/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.data;

public class PuppetWrapperError {
    private short code;
    private String description;

    public static final short PUPPETWRAPPER_INTERNAL_SERVER_ERROR = -500;
    public static final short PUPPETWRAPPER_ELEMENT_NOT_FOUND = -404;

    /**
     * 
     */
    public PuppetWrapperError() {
    }

    /**
     * @param code
     * @param description
     */
    public PuppetWrapperError(short code, String description) {
        super();
        this.code = code;
        this.description = description;
    }

    /**
     * @return the code
     */
    public short getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(short code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[Error]");
        sb.append("[code = ").append(this.code).append("]");
        sb.append("[description = ").append(this.description).append("]");
        sb.append("]");
        return sb.toString();
    }

}
