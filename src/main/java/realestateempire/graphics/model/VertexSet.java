package realestateempire.graphics.model;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL30.*;

class VertexSet {

    private final int drawCount;
    private final int vertexId;
    private final int textureId;

    VertexSet(int x, int y, int width, int height) {
        float[] vertices = createVertices(x, y, width, height);
        drawCount = vertices.length / 3;
        vertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        float[] textureCoords = new float[] {
                0, 0,
                1, 0,
                1, 1,

                1, 1,
                0, 1,
                0, 0,
        };
        textureId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(textureCoords), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    void render() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private float[] createVertices(int x, int y, int width, int height) {
        int halfResolutionWidth = 1280;
        int halfResolutionHeight = 720;

        float xLeft = (float) (x - halfResolutionWidth) / halfResolutionWidth;
        float xRight = (float) (x + width - halfResolutionWidth) / halfResolutionWidth;
        float yTop = (float) (halfResolutionHeight - y) / halfResolutionHeight;
        float yBottom = (float) (halfResolutionHeight - y - height) / halfResolutionHeight;

        return new float[] {
                xLeft, yTop, 0f,
                xRight, yTop, 0f,
                xRight, yBottom, 0f,

                xRight, yBottom, 0f,
                xLeft, yBottom, 0f,
                xLeft, yTop, 0f,
        };
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
