
var repeat=1 //enter 0 to not repeat scrolling after 1 run, otherwise, enter 1
var title=" EAC DEVELOPMENT TEAM - Jerome Jones, Chirag Joshi..............................";
var leng=title.length
var start=1
function titlemove() {
  titl=title.substring(start, leng) + title.substring(0, start)
  document.title=titl
  start++
  if (start==leng+1) {
    start=0
    if (repeat==0)
    return
    else
    titl=""
  }
  setTimeout("titlemove()",140)
}

var clickcount = 1;

function splash() {
  clickcount ++;
  
  if (clickcount == 3) {        
    var count;
    titlemove();
  }
}
