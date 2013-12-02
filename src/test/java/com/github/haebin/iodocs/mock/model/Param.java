package com.github.haebin.iodocs.mock.model;

import javax.validation.constraints.NotNull;

import com.github.haebin.iodocs.annotation.IoDocsDescription;

public class Param {
	@NotNull
	@IoDocsDescription("This key is for blah blah.")
	private long key = -1;
	
	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}
}
