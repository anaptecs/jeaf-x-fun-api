/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.common;

import java.io.Serializable;

import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class can be used to identify a software component within an application.
 */
public final class ComponentID implements Serializable {
  /**
   * Serial version uid of this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Attribute contains the name of the component. The name can be used to identity a component, since it has to be
   * unique. The attribute is never null, because it has to be passed to the constructor when the object is created.
   */
  private final String componentName;

  /**
   * Name of the base package in which the component is located. The attribute is never null, because its hat to be
   * passed to the constructor when the object is created.
   */
  private final String basePackage;

  /**
   * Initialize component id.
   * 
   * @param pComponentName Name of the component which can be used to identify the component. The parameter must not be
   * null.
   * @param pBasePackage Name of the base package in which the component is locate. The parameter must not be null.
   */
  public ComponentID( String pComponentName, String pBasePackage ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pComponentName, "pComponentName");
    Check.checkInvalidParameterNull(pBasePackage, "pBasePackage");

    // Assign passed component id and base package to class' attribute.
    componentName = pComponentName;
    basePackage = pBasePackage;
  }

  /**
   * Method returns the name of the component that can be identified by this object.
   * 
   * @return String Name of the component. The method never returns null.
   */
  public String getComponentName( ) {
    return componentName;
  }

  /**
   * Method returns the base package in which the component is located.
   * 
   * @return String Name of the base package of the component. The method never returns null.
   */
  public String getBasePackage( ) {
    return basePackage;
  }

  /**
   * Method checks whether the passed object represents the same component as this object. Therefore the following
   * requirements must match:
   * <ul>
   * <li>pObject must not be null.</li>
   * <li>pObject must be instance of this class.</li>
   * <li>Method getComponentName() must return the same value for this object and for pObject.</li>
   * </ul>
   * 
   * @param pObject Object that should be compared with this component id. The parameter may be null.
   * @return boolean Method returns true if the compared objects represent the same component and false in all other
   * cases.
   * @see Object#equals(Object)
   */
  public boolean equals( Object pObject ) {
    boolean lEquals;
    if (pObject != null) {
      // May be it is the same object
      if (this == pObject) {
        lEquals = true;
      }
      // Not the same object ;-(
      else {
        // Check for same class.
        if (pObject instanceof ComponentID) {
          // Check for same component name.
          ComponentID lComponentID = (ComponentID) pObject;
          String lComponentName = lComponentID.getFullyQualifiedName();
          lEquals = this.getFullyQualifiedName().equals(lComponentName);
        }
        // Not even the same class
        else {
          lEquals = false;
        }
      }
    }
    // Null is not equal to this object
    else {
      lEquals = false;
    }
    // Return result.
    return lEquals;
  }

  /**
   * Method returns the hash code for this object. The implementation of the base class is overwritten because due to
   * the contract of equals(...) and hashCode() always both methods have to be overwritten in a way that these two
   * methods fit together. That's why this implementation of the method returns the hash code of the component name.
   * 
   * @return int The hash code of the component name is used as hash code for this object.
   * @see Object#hashCode()
   */
  public int hashCode( ) {
    return componentName.hashCode();
  }

  /**
   * Method returns a string representation of this object. Since the component name has to be unique the methode simply
   * returns the component name.
   * 
   * @return String Name of the component. The method never returns null.
   * @see java.lang.Object#toString()
   */
  public String toString( ) {
    return this.getComponentName();
  }

  /**
   * Method returns the fully qualified name of the component. This name consists of the fully qualified name of the
   * base package and the name of the component seperated by '.'.
   * 
   * @return String Fully qualified name of the component. The method never returns null.
   */
  public String getFullyQualifiedName( ) {
    StringBuilder lBuilder = new StringBuilder(basePackage.length() + componentName.length() + 1);
    lBuilder.append(basePackage);
    lBuilder.append('.');
    lBuilder.append(componentName);
    return lBuilder.toString();
  }
}
