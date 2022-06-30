#!/usr/bin/env node

/*!
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: MIT-0
*/

import * as cdk from 'aws-cdk-lib';
import { WebAppStack } from '../lib/web-app-stack';
import { AwsSolutionsChecks } from 'cdk-nag';

const app = new cdk.App();
new WebAppStack(app, 'WebAppStack');
cdk.Aspects.of(app).add(new AwsSolutionsChecks());