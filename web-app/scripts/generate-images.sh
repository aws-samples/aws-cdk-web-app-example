#!/bin/bash
# Generate placeholder product images using ImageMagick

mkdir -p images

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
        images/product${i}.jpg
done

echo "Generated 10 product images"
