/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.kubernetes.client.server.mock;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.mockwebserver.crud.Attribute;
import io.fabric8.mockwebserver.crud.AttributeSet;
import org.junit.Assert;
import org.junit.Test;


public class KubernetesAttributesExtractorTest {

  @Test
  public void shouldHandleNamespacedPathWithResource() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/api/v1/namespaces/myns/pods/mypod");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "pod"));
    expected = expected.add(new Attribute("namespace", "myns"));
    expected = expected.add(new Attribute("name", "mypod"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleNamespacedPath() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/api/v1/namespaces/myns/pods");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "pod"));
    expected = expected.add(new Attribute("namespace", "myns"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleNonNamespacedPath() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/api/v1/nodes/mynode");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "node"));
    expected = expected.add(new Attribute("name", "mynode"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandlePathWithParameters() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/api/v1/pods?labelSelector=testKey%3DtestValue");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "pod"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleResource() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    Pod pod = new PodBuilder().withNewMetadata().withName("mypod").withNamespace("myns").endMetadata().build();

    AttributeSet attributes = extractor.extract(pod);

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "pod"));
    expected = expected.add(new Attribute("namespace", "myns"));
    expected = expected.add(new Attribute("name", "mypod"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));

  }

  @Test
  public void shouldHandleKindWithoutVersion() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/api/pods");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "pod"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleExtensions() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/apis/extensions/v1beta1/deployments");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "deployment"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleIngress() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/apis/extensions/v1beta1/namespaces/myns/ingresses/myingress");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "ingress"));
    expected = expected.add(new Attribute("namespace", "myns"));
    expected = expected.add(new Attribute("name", "myingress"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleIngresses() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/apis/extensions/v1beta1/namespaces/myns/ingresses");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "ingress"));
    expected = expected.add(new Attribute("namespace", "myns"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleApiGroups() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/apis/autoscaling/v1/namespaces/myns/horizontalpodautoscalers/myhpa");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "horizontalpodautoscaler"));
    expected = expected.add(new Attribute("namespace", "myns"));
    expected = expected.add(new Attribute("name", "myhpa"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleCrds() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/apis/test.com/v1/namespaces/myns/crds/mycrd");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "crd"));
    expected = expected.add(new Attribute("namespace", "myns"));
    expected = expected.add(new Attribute("name", "mycrd"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

  @Test
  public void shouldHandleDashes() {
    KubernetesAttributesExtractor extractor = new KubernetesAttributesExtractor();
    AttributeSet attributes = extractor.extract("/apis/test.com/v1/namespaces/my-namespace/crds/my-dashed-type");

    AttributeSet expected = new AttributeSet();
    expected = expected.add(new Attribute("kind", "crd"));
    expected = expected.add(new Attribute("namespace", "my-namespace"));
    expected = expected.add(new Attribute("name", "my-dashed-type"));
    Assert.assertTrue("Expected " + attributes + " to match " + expected, attributes.matches(expected));
  }

}
