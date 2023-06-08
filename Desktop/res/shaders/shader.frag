#version 330 core

uniform sampler2D _texture;

in vec3 vertexColor;
in vec2 TexCoord;

out vec4 FragColor;

void main()
{
    vec4 texel = texture(_texture, TexCoord);
    FragColor = texel;

    // this interpolates between the texel and the vertex color
//    FragColor = mix(texel, FragColor, 0.5);
}
