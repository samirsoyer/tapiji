/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API and implementation
 ******************************************************************************/
package org.eclipselabs.tapiji.translator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Glossary implements Serializable {

    private static final long serialVersionUID = 2070750758712154134L;

    public Info info;

    @XmlElementWrapper(name = "terms")
    @XmlElement(name = "term")
    public List<Term> terms;

    public Glossary() {
	terms = new ArrayList<Term>();
	info = new Info();
    }

    public Term[] getAllTerms() {
	return terms.toArray(new Term[terms.size()]);
    }

    public int getIndexOfLocale(String referenceLocale) {
	int i = 0;

	for (String locale : info.translations) {
	    if (locale.equalsIgnoreCase(referenceLocale))
		return i;
	    i++;
	}

	return 0;
    }

    public void removeTerm(Term elem) {
	for (Term term : terms) {
	    if (term == elem) {
		terms.remove(term);
		break;
	    }

	    if (term.removeTerm(elem))
		break;
	}
    }

    public void addTerm(Term parentTerm, Term newTerm) {
	if (parentTerm == null) {
	    this.terms.add(newTerm);
	    return;
	}

	for (Term term : terms) {
	    if (term == parentTerm) {
		term.subTerms.add(newTerm);
		break;
	    }

	    if (term.addTerm(parentTerm, newTerm))
		break;
	}
    }

}
