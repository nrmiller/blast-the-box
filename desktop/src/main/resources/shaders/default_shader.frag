#version 330 core

uniform sampler2D _texture;

in vec3 vertexColor;
in vec2 TexCoord;

out vec4 FragColor;

uniform float opacity;

void main()
{
    vec4 texel = texture(_texture, TexCoord);
    texel.a = texel.a * opacity;
    FragColor = texel;

    // this interpolates between the texel and the vertex color
//    FragColor = mix(texel, vec4(vertexColor.rgb, 1.0), 0.5);
}
