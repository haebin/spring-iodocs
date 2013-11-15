package com.github.haebin.iodocs.mock.model;

import javax.validation.constraints.NotNull;

import com.github.haebin.iodocs.annotation.IoDocsDescription;

public class Param {
	@NotNull
	@IoDocsDescription(" 파라미터 key 지롱~ ")
	private long key = -1;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}
}
