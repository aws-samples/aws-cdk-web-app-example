/*!
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: MIT-0
*/

import * as cdk from 'aws-cdk-lib'
import { Template, Match, Capture } from 'aws-cdk-lib/assertions'
import * as WebApp from '../lib/web-app-stack'


describe('Web App Stack', () => {

  const app = new cdk.App();
  const stack = new WebApp.WebAppStack(app, 'WebAppStack');
  const template = Template.fromStack(stack);

  test('matches the the snapshot', () => {
    expect(template.toJSON()).toMatchSnapshot()
  })

})


