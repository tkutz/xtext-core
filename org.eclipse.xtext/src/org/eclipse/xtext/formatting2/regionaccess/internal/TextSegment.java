/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.formatting2.regionaccess.internal;

import org.eclipse.xtext.formatting2.regionaccess.ITextRegionAccess;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class TextSegment extends AbstractTextSegment {

	private final int length;
	private final int offset;
	private final ITextRegionAccess regionAccess;

	public TextSegment(ITextRegionAccess regionAccess, int offset, int length) {
		super();
		this.regionAccess = regionAccess;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public ITextRegionAccess getTextRegionAccess() {
		return regionAccess;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this).singleLine().add("offset", offset).add("length", length);
		if (regionAccess != null)
			builder.add("text", getText());
		return builder.toString();
	}

}
