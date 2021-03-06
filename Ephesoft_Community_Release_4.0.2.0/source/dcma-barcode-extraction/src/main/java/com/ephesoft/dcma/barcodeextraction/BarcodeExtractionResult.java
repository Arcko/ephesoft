/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.barcodeextraction;

import com.ephesoft.dcma.barcodeextraction.BarcodeExtractionReader.BarcodeReaderTypes;

/**
 * This is a bean which stores the results of the scanning barcodes on each image file using Zxing barcode plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.barcode.BarcodeExtractionReader
 * 
 */
public class BarcodeExtractionResult {

	/**
	 * x co-ordinate from page left to the top-left corner of barcode.
	 */
	private double x0Coordinate;

	/**
	 * y co-ordinate from page top to the top-left corner of barcode.
	 */
	private double y0Coordinate;

	/**
	 * x co-ordinate from page left to the bottom-right corner of barcode.
	 */
	private double x1Coordinate;

	/**
	 * y co-ordinate from page left to the top-left corner of barcode.
	 */
	private double y1Coordinate;

	/**
	 * The value on barcode.
	 */
	private String texts;

	/**
	 * The type of Barcode viz Code 39, QR, Datamatrix etc.
	 */
	private BarcodeReaderTypes barcodeType;

	public double getX0() {
		return x0Coordinate;
	}

	public void setX0Coordinate(final double x0Coordinate) {
		this.x0Coordinate = x0Coordinate;
	}

	public double getY0Coordinate() {
		return y0Coordinate;
	}

	public void setY0Coordinate(final double y0Coordinate) {
		this.y0Coordinate = y0Coordinate;
	}

	public double getX1Coordinate() {
		return x1Coordinate;
	}

	public void setX1Coordinate(final double x1Coordinate) {
		this.x1Coordinate = x1Coordinate;
	}

	public double getY1Coordinate() {
		return y1Coordinate;
	}

	public void setY1Coordinate(final double y1Coordinate) {
		this.y1Coordinate = y1Coordinate;
	}

	public String getTexts() {
		return texts;
	}

	public void setTexts(final String texts) {
		this.texts = texts;
	}

	public BarcodeReaderTypes getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(final BarcodeReaderTypes barcodeType) {
		this.barcodeType = barcodeType;
	}

}
