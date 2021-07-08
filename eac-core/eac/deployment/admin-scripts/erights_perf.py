#!/usr/bin/python
'''
Created on 1 May 2012

@author: Will
'''
from getopt import getopt
import sys
import os
import datetime
import time

_USAGE = "Usage: %s [--methods comma seperated list of methods] [--from dd/mm/yy] [--to dd/mm/yy] [--dir /path/to/logs/directory]" % os.path.splitext(os.path.basename(__file__))[0]
_LOG_FILENAME = "erights-performance.log"


def main():
    """
    Entry point - invoked by __main__().
    """
    config = _config()
    tracker = _StatisticsTracker(config)
    _process_logs(tracker, config)
    tracker.pretty_print()
    
def _process_logs(tracker, config):
    """
    Iterates through the log files (in reverse chronological order) and passes
    each line to the statistics tracker.
    
    Args:
        tracker: The _StatisticsTracker responsible for determining whether to track the
                statistics for the given log line.
        config: The script configuration.
    """
    log_filenames = _get_log_filenames(config["dir"])
    for log_filename in log_filenames:
        log_file = None
        try:
            log_file = open(log_filename, "r")
            for line in log_file:
                log_line = _LogLine(line)
                tracker.track(log_line)
        finally:
            if log_file:
                log_file.close()
    
def _config():
    """
    Gets the configuration for the script based on the options passed and their corresponding values.

    Returns:
        A dict mapping the configuation name to its corresponding value.
    """
    config = {}
    args = sys.argv[1:]
    if "--help" in args:
        print(_USAGE)
        sys.exit(0)
    opt_list, args = getopt(args, "" , ["methods=", "from=", "to=", "dir="])
    for opt in opt_list:
        opt_name = _strip_prefix(opt[0])
        if opt_name == "methods":
            config[opt_name] = opt[1].split(",")
        elif opt_name == "from":
            t = time.strptime(opt[1], "%d/%m/%y")
            config[opt_name] = datetime.datetime(*t[:6])
        elif opt_name == "to":
            t = time.strptime(opt[1], "%d/%m/%y")
            config[opt_name] = datetime.datetime(*t[:6]).replace(hour=23, minute=59, second=59)
        else:
            config[opt_name] = opt[1]
    if "methods" not in config:
        config["methods"] = "all"
    if "from" not in config:
        config["from"] = datetime.datetime(1970, 1, 1)
    if "to" not in config:
        config["to"] = datetime.datetime.today()
    if "dir" not in config:
        config["dir"] = "."
    return config

def _strip_prefix(option_name):
    """
    Removes any leading dashes from the start of the option name.
    
    Args:
        option_name: The name of the option from which to remove the leading dashes.
    Returns:
        The name minus any leading dashes.
    """
    no_prefix = ""
    for c in option_name:
        if c != "-":
            no_prefix += c
    return no_prefix

def _get_log_filenames(directory):
    """
    Gets a list of the log filenames in the specified directory.
    
    Args:
        directory: The directory to look for log files.
        
    Returns:
        A list of log file names reverse sorted by name (i.e. chronological order).
    """
    all_filenames = os.listdir(directory)
    log_filenames = []
    for filename in all_filenames:
        if filename.startswith(_LOG_FILENAME):
            log_filenames.append(os.path.join(directory, filename))
    def get_numeric_suffix(filename):
        suffix = 0
        if filename[1:].count(".") == 2:
            suffix = os.path.splitext(filename)[1][1:]
        return int(suffix)
    log_filenames.sort(key=get_numeric_suffix, reverse=True)
    return log_filenames


class _LogLine:
    """
    Representation of a record within the log file. Expects the line format to be 
    comma separated: date,method,duration
    """
    def __init__(self, line):
        fields = line.split(",")
        t = time.strptime(fields[0], "%d/%m/%y %H:%M")
        self.date = datetime.datetime(*t[:6])  
        self.method = fields[1]
        self.duration = int(fields[2]) 
        
    def __repr__(self):
        return "%s, %s, %s" % (self.date, self.method, self.duration)
        
class _StatisticsTracker:
    """
    Tracks the current state of the statistics for a given method.
    """
    def __init__(self, config):
        self.config = config
        self.stats_by_method = {}
        
    def track(self, log_line):
        """
        Tracks the statistics contained in the given log_line - based on the options
        that the _StatisticsTracker was initialised with.
        
        Args:
            log_line: The current line of the log file being parsed containing the data to track.
        """
        if log_line.method in self.config["methods"] or "all" in self.config["methods"]:
            if log_line.date >= self.config["from"] and log_line.date <= self.config["to"]:
                stats = self.stats_by_method.get(log_line.method)
                if not stats:
                    stats = _MethodStatistics(log_line.method)
                    self.stats_by_method[log_line.method] = stats
                stats.add(log_line.duration)
    
    def pretty_print(self):
        """
        Prints the state of the statistics.
        """
        if len(self.stats_by_method) > 0:
            print("Method\tMin\tMax\tAverage\t90th Percentile\tCount")
            sorted_keys = sorted(self.stats_by_method.keys())
            for key in sorted_keys:
                stats = self.stats_by_method[key]
                sys.stdout.write(stats.method)
                sys.stdout.write("\t")
                sys.stdout.write(str(stats.min))
                sys.stdout.write("\t")
                sys.stdout.write(str(stats.max))
                sys.stdout.write("\t")
                sys.stdout.write(str(stats.average()))
                sys.stdout.write("\t")
                sys.stdout.write(str(stats.ninetieth_percentile()))
                sys.stdout.write("\t")
                sys.stdout.write(str(stats.count))
                sys.stdout.write("\n")
        else:
            print("No statistics to display")
            
        
class _MethodStatistics:
    """
    Holds the statistics for a given webservice method.
    """
    def __init__(self, method):
        self.method = method
        self.min = sys.maxint
        self.max = 0
        self.total = 0
        self.count = 0
        self._durations = []

    def add(self, duration):
        if duration < self.min:
            self.min = duration
        if duration > self.max:
            self.max = duration
        self.total += duration
        self.count += 1
        self._durations.append(duration)
        
    def average(self):
        if self.count == 0:
            return 0
        return self.total / self.count
    
    def ninetieth_percentile(self):
        sorted_durations = sorted(self._durations)
        n = int(round(0.9 * self.count + 0.5))
        return sorted_durations[n-1]

    
if __name__ == '__main__':
    main()
    