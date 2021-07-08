package com.oup.eac.data;

import com.oup.eac.domain.ProgressBar;
import com.oup.eac.domain.ProgressState;

public interface ProgressBarDao extends BaseDao<ProgressBar, String> {

	ProgressBar getProgressBar(ProgressState state, String page);
}
