# aws-cdk-web-app-example

This is an example of how to build a resilient web application with just 30 lines lines of TypeScript code by using the AWS CDK. It consists of one Amazon Virtual Private Cloud (VPC) with private and public subnets in multiple Availability Zones, in which an Application Load Balancer and an Amazon EC2 Auto Scaling Group are deployed. The Application Load Balancer accepts requests via HTTP (port 80) and forwards them to the instances of the Auto Scaling Group. The Auto Scaling Group is using a Launch Configuration that contains a User Data script to install the Apache HTTP Server and serve a static HTML page.

> **Note:** This example stack is intentionally not production-ready, in order to keep the sample short. Among other improvements, please take note of the suppressions that have been added to the stack for [cdk-nag](https://github.com/cdklabs/cdk-nag/). 

## Deploying the CDK Stack

Set up CDK as described [here](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html). Then, use the following commands to install all dependencies and deploy the stack in your AWS account:

```
cd web-app
npm install
npm run build
cdk deploy
```

## Accessing the test page

After deploying, the CDK CLI will return an output called `WebAppStack.Hostname` which is the hostname of the Application Load Balancer. Open this hostname in a web browser (via HTTP) to display the HTML page. Note that immediately after the deployment, you might see an error code of 502 because the instances of Auto Scaling Group have not finished installing the Apache HTTP Server yet. In this case, wait a few minutes and try again.

## Testing the stack

This repository was scaffolded via `cdk init sample-app --language typescript`. This command also scaffolds a test workflow with [jest](https://jestjs.io/docs/getting-started). The test suite can be run with the command `npm run test`. To keep this example simple, the test suite only employs the method of [snapshot testing](https://docs.aws.amazon.com/cdk/v2/guide/testing.html#testing_snapshot).