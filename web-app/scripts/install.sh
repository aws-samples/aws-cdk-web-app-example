#!/bin/bash -xe

# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: MIT-0

yum update -y
yum install -y httpd ImageMagick
systemctl start httpd
systemctl enable httpd
usermod -a -G apache ec2-user
chown -R ec2-user:apache /var/www
chmod 2775 /var/www
find /var/www -type d -exec chmod 2775 {} \;
find /var/www -type f -exec chmod 0664 {} \;

# Create images directory
mkdir -p /var/www/html/images

# Generate placeholder product images
colors=("#FF6B6B" "#4ECDC4" "#45B7D1" "#FFA07A" "#98D8C8" "#F7DC6F" "#BB8FCE" "#85C1E2" "#F8B739" "#52B788")
products=("Headphones" "Watch" "Stand" "Keyboard" "Hub" "Webcam" "Lamp" "Mouse" "Phone Stand" "Organizer")

for i in {1..10}; do
    color="${colors[$((i-1))]}"
    product="${products[$((i-1))]}"
    
    convert -size 400x300 xc:"$color" \
        -gravity center \
        -pointsize 40 \
        -fill white \
        -annotate +0+0 "$product" \
        /var/www/html/images/product${i}.jpg
done

# HTML content will be added by CDK from static/index.html

# Set permissions
chown -R ec2-user:apache /var/www/html
chmod -R 755 /var/www/html
