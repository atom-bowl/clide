#!/usr/bin/env python3
"""
Simple icon generator for Clide
Creates a basic .ico file with a "C" letter
"""

from PIL import Image, ImageDraw, ImageFont
import os

def create_clide_icon():
    """Create a simple Clide icon"""
    # Create a new image with transparency
    sizes = [256, 128, 64, 48, 32, 16]
    images = []

    for size in sizes:
        # Create image with gradient background
        img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)

        # Draw rounded rectangle background
        padding = size // 8
        draw.rounded_rectangle(
            [(padding, padding), (size - padding, size - padding)],
            radius=size // 6,
            fill=(30, 120, 210, 255),  # Blue color
            outline=(20, 80, 170, 255),
            width=max(1, size // 32)
        )

        # Draw letter "C" in white
        font_size = size * 3 // 5
        try:
            # Try to use a nice font if available
            font = ImageFont.truetype("arial.ttf", font_size)
        except:
            # Fallback to default font
            font = ImageFont.load_default()

        text = "C"
        # Get text bounding box
        bbox = draw.textbbox((0, 0), text, font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]

        # Center the text
        x = (size - text_width) // 2
        y = (size - text_height) // 2 - bbox[1]

        # Draw text with shadow
        shadow_offset = max(1, size // 64)
        draw.text((x + shadow_offset, y + shadow_offset), text,
                 fill=(0, 0, 0, 128), font=font)
        draw.text((x, y), text, fill=(255, 255, 255, 255), font=font)

        images.append(img)

    # Save as .ico
    output_path = os.path.join('src', 'main', 'resources', 'icons', 'clide.ico')
    images[0].save(output_path, format='ICO', sizes=[(s, s) for s in sizes])
    print(f"[OK] Icon created at: {output_path}")

    # Also save as PNG for preview
    png_path = os.path.join('src', 'main', 'resources', 'icons', 'clide.png')
    images[0].save(png_path, format='PNG')
    print(f"[OK] PNG preview created at: {png_path}")

if __name__ == '__main__':
    try:
        create_clide_icon()
        print("\n[SUCCESS] Clide icon created successfully!")
        print("\nTo use it:")
        print("1. Uncomment the icon line in pom.xml")
        print("2. Rebuild with: .\\build-exe.ps1")
    except ImportError:
        print("Error: PIL/Pillow not installed")
        print("\nInstall it with: pip install Pillow")
        print("\nOr use an online tool:")
        print("- https://convertio.co/png-ico/")
        print("- https://www.icoconverter.com/")
