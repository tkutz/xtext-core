/**
 * Copyright (c) 2017 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ide.tests.testlanguage.ide.serializer;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.formatting2.regionaccess.ITextRegionDiffBuilder;
import org.eclipse.xtext.ide.serializer.hooks.IReferenceUpdater;
import org.eclipse.xtext.ide.serializer.hooks.IReferenceUpdaterContext;
import org.eclipse.xtext.ide.serializer.hooks.IUpdatableReference;
import org.eclipse.xtext.ide.serializer.impl.EObjectDescriptionDeltaProvider;
import org.eclipse.xtext.ide.serializer.impl.RelatedResourcesProvider;
import org.eclipse.xtext.ide.tests.testlanguage.partialSerializationTestLanguage.Import;
import org.eclipse.xtext.ide.tests.testlanguage.partialSerializationTestLanguage.Node;
import org.eclipse.xtext.ide.tests.testlanguage.partialSerializationTestLanguage.PartialSerializationTestLanguageFactory;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("all")
public class ChangeSerializerTestCleanupStrategy implements IReferenceUpdater {
  @Inject
  private IQualifiedNameConverter converter;
  
  @Override
  public void update(final IReferenceUpdaterContext context) {
    final Node node = IterableExtensions.<Node>head(Iterables.<Node>filter(context.getResource().getContents(), Node.class));
    if (((node == null) || node.getImports().isEmpty())) {
      return;
    }
    final HashSet<Import> toDelete = CollectionLiterals.<Import>newHashSet();
    final HashSet<QualifiedName> toAdd = CollectionLiterals.<QualifiedName>newHashSet();
    final HashMap<Import, QualifiedName> toChange = CollectionLiterals.<Import, QualifiedName>newHashMap();
    final Function1<Import, QualifiedName> _function = (Import it) -> {
      return this.converter.toQualifiedName(it.getImportedNamespace());
    };
    final Map<QualifiedName, Import> imports = IterableExtensions.<QualifiedName, Import>toMap(node.getImports(), _function);
    if ((((!toDelete.isEmpty()) || (!toAdd.isEmpty())) || (!toChange.isEmpty()))) {
      final Runnable _function_1 = () -> {
        for (final Import toDel : toDelete) {
          EcoreUtil.remove(toDel);
        }
        Set<Map.Entry<Import, QualifiedName>> _entrySet = toChange.entrySet();
        for (final Map.Entry<Import, QualifiedName> toCh : _entrySet) {
          {
            final Import imp = toCh.getKey();
            final QualifiedName name = toCh.getValue();
            imp.setImportedNamespace(this.converter.toString(name));
          }
        }
        for (final QualifiedName toA : toAdd) {
          EList<Import> _imports = node.getImports();
          Import _createImport = PartialSerializationTestLanguageFactory.eINSTANCE.createImport();
          final Procedure1<Import> _function_2 = (Import it) -> {
            it.setImportedNamespace(this.converter.toString(toA));
          };
          Import _doubleArrow = ObjectExtensions.<Import>operator_doubleArrow(_createImport, _function_2);
          _imports.add(_doubleArrow);
        }
      };
      context.modifyModel(_function_1);
    }
  }
  
  @Override
  public boolean isAffected(final EObjectDescriptionDeltaProvider.Deltas deltas, final RelatedResourcesProvider.RelatedResource resource) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  public void updateReference(final ITextRegionDiffBuilder rewriter, final IUpdatableReference updatable) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
