/*******************************************************************************
 * Copyright (c) 2012 TapiJI.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.babel.tapiji.translator.rbe.model.analyze;

public interface ILevenshteinDistanceAnalyzer {

	double analyse(Object value, Object pattern);

}
