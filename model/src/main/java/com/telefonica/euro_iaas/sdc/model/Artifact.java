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

package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * Represents an artifact to be installed on a ProductRelease.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Artifact {

    public static final String VDC_FIELD = "vdc";

    public static final String PRODUCT_FIELD = "productInstance";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String vdc;

    // @ManyToOne(optional=false)
    // private ProductInstance productInstance;

    // @ManyToOne
    // @JoinColumn(name = "productinstance_id")
    // private ProductInstance productInstance;

    // @ManyToOne(optional=false)
    // @JoinColumn(name="productinstance_id")
    // @JoinColumn(name = "id")
    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "productinstance_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = ProductInstance.class, optional = false, fetch = FetchType.LAZY)
    @XmlTransient
    private ProductInstance productInstance;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    /**
     * Default Constructor
     */
    public Artifact() {

    }

    /**
     * @param name
     * @param path
     * @param artifactType
     * @param productRelease
     */
    public Artifact(String name, List<Attribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    /**
     * @param name
     * @param path
     * @param artifactType
     * @param productRelease
     */
    public Artifact(String name, String vdc, ProductInstance productInstance, List<Attribute> attributes) {
        this.name = name;
        this.vdc = vdc;
        this.productInstance = productInstance;
        this.attributes = attributes;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * @return the attributes
     */

    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the attributes
     */

    public ProductInstance getProductInstance() {
        return this.productInstance;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    /**
     * Add a new attribute.
     * 
     * @param attribute
     *            the attribute
     */
    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(attribute);
    }

    /**
     * @return the attributes as a Map
     */
    public Map<String, String> getMapAttributes() {
        Map<String, String> atts = new HashMap<String, String>();
        for (Attribute att : attributes) {
            atts.put(att.getKey(), att.getValue());
        }
        return atts;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Artifact other = (Artifact) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (!name.equals(other.name)) {
            return false;
        }

        return true;
    }
    @Override
   public int hashCode() {
       final int prime = 31;
       int result = 1;
       result = prime * result + ((id == null) ? 0 : id.hashCode());
       return result;
   }

}
