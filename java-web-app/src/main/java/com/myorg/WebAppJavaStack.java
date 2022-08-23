package com.myorg;

import software.constructs.Construct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.autoscaling.AutoScalingGroup;
import software.amazon.awscdk.services.autoscaling.AutoScalingGroupProps;
import software.amazon.awscdk.services.autoscaling.CfnAutoScalingGroup;
import software.amazon.awscdk.services.ec2.AmazonLinuxGeneration;
import software.amazon.awscdk.services.ec2.AmazonLinuxImageProps;
import software.amazon.awscdk.services.ec2.AmazonLinuxKernel;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetsProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListener;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer;
import software.amazon.awscdk.services.elasticloadbalancingv2.BaseApplicationListenerProps;

public class WebAppJavaStack extends Stack {
    public WebAppJavaStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public WebAppJavaStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        Vpc vpc = new Vpc(this, "MyVPC");

        AutoScalingGroup asg = AutoScalingGroup.Builder.create(this, "MyASG")
            .vpc(vpc)
            .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
            // see https://github.com/aws/aws-cdk/issues/21011
            .machineImage(MachineImage.fromSsmParameter("/aws/service/ami-amazon-linux-latest/al2022-ami-kernel-5.15-x86_64"))
            /* 
            .machineImage(MachineImage.latestAmazonLinux(AmazonLinuxImageProps.builder()
                .generation(AmazonLinuxGeneration.AMAZON_LINUX_2022)
                .build()))
            */
            .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_NAT).build())
            .minCapacity(2)
            .build();

        Path fileName = Path.of("../web-app/scripts/install.sh");

        String script = Files.readString(fileName);

        asg.addUserData(script);

        ApplicationLoadBalancer alb = ApplicationLoadBalancer.Builder.create(this, "MyALB")
            .vpc(vpc)
            .internetFacing(true)
            .build();

        ApplicationListener listener = alb.addListener("HttpListener", BaseApplicationListenerProps.builder()
            .port(80)
            .build());

        listener.addTargets("Targets", AddApplicationTargetsProps.builder()
            .port(80)
            .targets(Collections.singletonList(asg))
            .build());

        listener.getConnections().allowDefaultPortFromAnyIpv4("Allow access to port 80 from the internet.");

        CfnOutput.Builder.create(this, "Hostname").value(alb.getLoadBalancerDnsName()).build();

    }
}
