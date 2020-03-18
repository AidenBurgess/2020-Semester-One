#include <windows.h>
#include <gl/gl.h>
#include <gl/glu.h>
#include <glut.h>
#include <math.h>
#include <iostream>
#include "Trackball.h"
using namespace std;

	// geometry constants
    const double PI = 3.1415926;
    const double FOVDEGS = 45;   // Default field of view (degrees)
    const double FOV = FOVDEGS * PI / 180;  // Ibid (radians)
    const double fovActual = 2 * atan(tan(FOV / 2)) * 180.0 / PI;
	const int X = 0, Y = 1, Z = 2;

    // Lighting and material constants
    const float BLACK[] = {0,0,0,1};
    const float WHITE[] = {1,1,1,1};
    const float GREY[] = {0.4,0.4,0.4,1};
    const float AMBIENT = 0.3f;
    const float LIGHT_AMBIENT[] = {AMBIENT, AMBIENT, AMBIENT};
    const float GROUND_COLOUR[] = {0.7,0.7,0.7,1};
    const float MATERIAL_AMBIENT[] = {0.7, 0.5, 0.2};
    const float MATERIAL_DIFFUSE[] = {0.7, 0.5, 0.2};
    const float MATERIAL_SPECULAR[] = {0.7, 0.7, 0.7};
    const float MATERIAL_SHININESS = 50;
    
    const float SHADOW_COLOUR[] = {0.0, 0.0, 0.0, 0.6};

    const float METAL_SHININESS = 10;

    const int TESSELLATIONS = 50;    // For sphere

	// interaction constants
    const float DELTA_HEIGHT = 0.05f;
	const float DELTA_LIGHT = 0.2f;
	//const float LIGHT_INIT_X = 10.0f, LIGHT_INIT_Y = 40.0f, LIGHT_INIT_Z = 50.0f;
	const float LIGHT_INIT_X = 0.0f, LIGHT_INIT_Y = 10.0f, LIGHT_INIT_Z = 0.0f;
	const float EYE_INIT_X = 0.0f, EYE_INIT_Y = 0.0f, EYE_INIT_Z = 10.0f;
	const float LOOK_AT_X = 0.0f, LOOK_AT_Y = 0.5f, LOOK_AT_Z = 0.0f;
	const float DEYE_DLOOK_X = EYE_INIT_X-LOOK_AT_X, DEYE_DLOOK_Y = EYE_INIT_Y-LOOK_AT_Y,
				DEYE_DLOOK_Z = EYE_INIT_Z-LOOK_AT_Z;
	const float EYE_POSITION_SHIFT_FACTOR = 0.05f;

	float sceneShift = 0;   // Vertical shift of scene (to show point of shadows)
    float cubeAndSphereShift = 0.3;
    float groundSq[][3] = {{-3,-0.0001,3}, {3,-0.001,3},{3,-0.001,-3},{-3,-0.001,-3}};

	// light and eye position global variables
    float light_position[] = {LIGHT_INIT_X, LIGHT_INIT_Y, LIGHT_INIT_Z,1};
	float eye[] = {EYE_INIT_X, EYE_INIT_Y, EYE_INIT_Z};
    float eyePositionShift = 0.0f;

    // Vertex, face and normal data for the cube
    const double vertices[][3] = {{0,0,0},{1,0,0},
        {0,1,0},{1,1,0},{0,0,1},{1,0,1},{0,1,1},{1,1,1}};
    const double normals[][3] = {{0,0,-1},{0,0,1},{0,1,0},
        {0,-1,0},{-1,0,0},{1,0,0}};
    const int faces[6][4] = {
            // order is back, front, top, bottom, left, right
            {0,2,3,1},{4,5,7,6},{2,6,7,3},{4,0,1,5},{6,2,0,4},{1,3,7,5}
    };

CTrackball trackball;
bool ambientEnabled = true, diffuseEnabled = true, specularEnabled = true;
bool shadowsEnabled = false;

//////////////////////////////////
//
// Trackball event handling functions
//
//////////////////////////////////

void handleMouseMotion(int x, int y)
{   
    trackball.tbMotion(x, y);
}

void handleMouseClick(int button, int state, int x, int y)
{
    trackball.tbMouse(button, state, x, y);
}


//////////////////////////////////
//
// Keyboard event handling functions
//
//////////////////////////////////
void handleKeyboardEvent(unsigned char key, int x, int y)
{
    if (key == 'w')
       shadowsEnabled = !shadowsEnabled;
    else if (key == 'h')
       sceneShift += DELTA_HEIGHT;
    else if (key == 'H')
       sceneShift -= DELTA_HEIGHT;
    else if (key == 'e')
	{
        if ( eyePositionShift < 1.0f )
			eyePositionShift += EYE_POSITION_SHIFT_FACTOR;
	}
    else if (key == 'E')
        eyePositionShift -= EYE_POSITION_SHIFT_FACTOR;
    else if (key == 'i')
	{ light_position[0] = LIGHT_INIT_X,
	  light_position[1] = LIGHT_INIT_Y,
	  light_position[2] = LIGHT_INIT_Z; }
    else if (key == 'A')
	{ light_position[0] = LIGHT_INIT_X,
	  light_position[1] = LIGHT_INIT_Y,
	  light_position[2] = LIGHT_INIT_Z;
      eyePositionShift = 0.0f;
      trackball.tbInit(GLUT_LEFT_BUTTON);
      sceneShift = 0.0f; }
	
    else if (key == 'a')
        ambientEnabled = ! ambientEnabled;
    else if (key == 'd')
        diffuseEnabled = ! diffuseEnabled;
    else if (key == 's')
        specularEnabled = ! specularEnabled;

    else if (key == 'q')
        exit(0);
    else
       trackball.tbKeyboard(key);
    glutPostRedisplay();
}

void handleSpecialKeyEvent(int key, int x, int y)
{
    if (key == GLUT_KEY_LEFT)
       light_position[0] -= DELTA_LIGHT;
    else if (key == GLUT_KEY_RIGHT)
       light_position[0] += DELTA_LIGHT;
    else if (key == GLUT_KEY_UP)
       light_position[1] += DELTA_LIGHT;
    else if (key == GLUT_KEY_DOWN)
       light_position[1] -= DELTA_LIGHT;
    else if (key == GLUT_KEY_PAGE_UP)
       light_position[2] -= DELTA_LIGHT;
    else if (key == GLUT_KEY_PAGE_DOWN)
       light_position[2] += DELTA_LIGHT;
    glutPostRedisplay();
}


//////////////////////////////////
//
// Initialization
//
//////////////////////////////////
    void init(  ) {
      glClearColor( 0.0, 0.0, 0.0, 1.0 );
      glShadeModel( GL_SMOOTH );
      glEnable(GL_DEPTH_TEST);
      glEnable(GL_NORMALIZE);
      glShadeModel(GL_SMOOTH);
      trackball.tbInit(GLUT_LEFT_BUTTON);
	  
      // Set up lighting
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT, BLACK);
      glLightfv(GL_LIGHT0, GL_AMBIENT, LIGHT_AMBIENT);
      glLightfv(GL_LIGHT0, GL_DIFFUSE, WHITE);
      glLightfv(GL_LIGHT0, GL_SPECULAR, WHITE);
      glLightfv(GL_LIGHT0, GL_POSITION, light_position);
      glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER, 1);
      glEnable(GL_LIGHTING);
      glEnable(GL_LIGHT0);

	  // print HELP instructions
	  cout << "Keyboard controls:\n";
	  cout << "a = toggle ambient\n";
	  cout << "d = toggle diffuse\n";
	  cout << "s = toggle specular\n";
	  cout << "L/R arrow keys = move light left/right (X)\n";
	  cout << "U/D arrow keys = move light up/down (Y)\n";
	  cout << "PAGE_UP/PAGE_DOWN keys = move light near/far (Z)\n";
	  cout << "i = move light to initial position\n";
	  cout << "e = move eye position towards lookAt position\n";
	  cout << "E = move eye position away from lookAt position\n";

	  cout << "w = toggle shadows\n";
	  cout << "h = increase height of scene objects\n";
	  cout << "H = decrease height of scene objects\n";
	  cout << "A = initialize ALL positions: Lights, Camera, Action!\n";
	  cout << "q = quit\n";

	  cout << "\nMouse controls trackball.\n";
    }

//////////////////////////////////
//
// Draw all scene objects
//
//////////////////////////////////
    void drawSceneObjects(int tess) {
        // Draws the scene comprised of a cube, sphere, cylinder and teapot.
        // All objects have the same surface properties, which are those
        // set up at the time of the call.

        glPushMatrix();
        glTranslated(0, sceneShift, 0);

        // Cube, centred at (-1,0.4,1)
        glPushMatrix();
        glTranslatef(-1,0.4 + cubeAndSphereShift,1);
        glutSolidCube(0.8);
		if ( ! ambientEnabled && ! diffuseEnabled && ! specularEnabled)
		{
			glDisable(GL_LIGHTING);
			glColor3f(0.8f, 0.8f, 0.8f);
			glutWireCube(0.8);
			glEnable(GL_LIGHTING);
		}
        glPopMatrix();

         // Sphere, centred at (1,0.4,-1)
        glPushMatrix();
        glTranslatef(1,0.4 + cubeAndSphereShift,-1);
        glutSolidSphere(0.4, tess, tess);
		if ( ! ambientEnabled && ! diffuseEnabled && ! specularEnabled)
		{
			glDisable(GL_LIGHTING);
			glColor3f(0.8f, 0.8f, 0.8f);
			glutWireSphere(0.4, tess, tess);
			glEnable(GL_LIGHTING);
		}
        glPopMatrix();

       // Teapot at (-1,0,-1)
        glPushMatrix();
        glTranslatef(-1 ,0.53, -1);
        glutSolidTeapot(0.7);
		if ( ! ambientEnabled && ! diffuseEnabled && ! specularEnabled)
		{
			glDisable(GL_LIGHTING);
			glColor3f(0.8f, 0.8f, 0.8f);
			glutWireTeapot(0.7);
			glEnable(GL_LIGHTING);
		}
        glPopMatrix();

        // Cone at (1,0.4,1).
        glPushMatrix();
        glTranslatef(1,0,1);
        glRotatef(-90,1,0,0);
        glTranslatef(0, 0, 0.2);
		glutSolidCone(0.3, 1.2, tess, 4);
		if ( ! ambientEnabled && ! diffuseEnabled && ! specularEnabled)
		{
			glDisable(GL_LIGHTING);
			glColor3f(0.8f, 0.8f, 0.8f);
			glutWireCone(0.3, 1.2, tess, 1);
			glEnable(GL_LIGHTING);
		}
        glPopMatrix();

        glPopMatrix();
    }
    

//////////////////////////////////
//
// Display callback function (renders scene)
//
//////////////////////////////////
    void display(  ) {
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
        glMatrixMode( GL_PROJECTION );
        glLoadIdentity();
        
        gluPerspective( (float) fovActual, 1, 0.5, 20.0 );

        glMatrixMode( GL_MODELVIEW );
        glLoadIdentity();

		// Modify the eye position
		eye[X] = EYE_INIT_X - DEYE_DLOOK_X * eyePositionShift;
		eye[Y] = EYE_INIT_Y - DEYE_DLOOK_Y * eyePositionShift;
		eye[Z] = EYE_INIT_Z - DEYE_DLOOK_Z * eyePositionShift;
        gluLookAt( eye[X], eye[Y], eye[Z], LOOK_AT_X, LOOK_AT_Y, LOOK_AT_Z, 0.0, 1.0, 0.0 );
		
        trackball.tbMatrix();

        // Set current light position and draw a sphere that represents the light
        glLightfv(GL_LIGHT0, GL_POSITION, light_position);
		glDisable(GL_LIGHTING);
		glPushMatrix();
		glTranslatef( light_position[0], light_position[1], light_position[2] );
		glColor3f( 0.0f, 0.0f, 0.0f );
		glutWireSphere( 0.2f, 12, 12 ) ;
		glColor3f( 1.0f, 1.0f, 1.0f );
		glutSolidSphere( 0.05f, 8, 8 ) ;
		glPopMatrix();
		glEnable(GL_LIGHTING);

        // Draw the "ground" polygon first
		if ( ambientEnabled )
            glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, GROUND_COLOUR);
		else
			glMaterialfv(GL_FRONT, GL_AMBIENT, BLACK);
		if ( diffuseEnabled )
            glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, GROUND_COLOUR);
		else
			glMaterialfv(GL_FRONT, GL_DIFFUSE, BLACK);
		if ( specularEnabled )
		{
			glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, BLACK);
			glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 0);
		}
		else
		{
			glMaterialfv(GL_FRONT, GL_SPECULAR, BLACK);
			glMaterialf(GL_FRONT, GL_SHININESS, 0);
		}

        float vertical[] = {0, 1, 0};
        glBegin(GL_POLYGON);
        glNormal3fv(vertical);
        for (int i = 0; i < 4; i++)
            glVertex3fv(groundSq[i]);
        glEnd();

        // Set up for shadowing, and draw the scene shadows first (if reqd)
        if (shadowsEnabled) {
           glPushMatrix();
           glDisable(GL_LIGHTING);
           glDisable(GL_DEPTH_TEST);
           glColor4fv(SHADOW_COLOUR);
           glEnable(GL_BLEND);
           glBlendFunc(GL_SRC_ALPHA,  GL_ONE_MINUS_SRC_ALPHA);
           float xl = light_position[0], yl = light_position[1], zl = light_position[2];
           float ld = light_position[1];
           glTranslated(xl, yl, zl);
           double projectionMatrix[] = {ld,0,0,0,  0,ld,0,-1,  0,0,ld,0,  0,0,0,0};
           glMultMatrixd(projectionMatrix);
           glTranslated(-xl, -yl, -zl);
           glEnable(GL_BLEND);
           drawSceneObjects(TESSELLATIONS);
           glDisable(GL_BLEND);
           glPopMatrix();
        }

        // Now draw the scene objects
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
		if ( ambientEnabled )
			glMaterialfv(GL_FRONT, GL_AMBIENT, MATERIAL_AMBIENT);
		if ( diffuseEnabled )
			glMaterialfv(GL_FRONT, GL_DIFFUSE, MATERIAL_DIFFUSE);
		if ( specularEnabled )
		{
            glMaterialfv(GL_FRONT, GL_SPECULAR, MATERIAL_SPECULAR);
			glMaterialf(GL_FRONT, GL_SHININESS, MATERIAL_SHININESS);
		}
        drawSceneObjects(TESSELLATIONS);


        glFlush();
        glutSwapBuffers();
        int error = glGetError();
        while (error != GL_NO_ERROR) {
         cout << "*** OpenGL error: = " << gluErrorString(error) << endl;
         error = glGetError();
        };
     }


//////////////////////////////////
//
// Window resize callback function
//
//////////////////////////////////
void reshape(int width, int height ) {
    // Called at start, and whenever user resizes component
    int size = min(width, height);
    glViewport(0, 0, size, size);  // Largest possible square
    trackball.tbReshape(width, height);
}


//////////////////////////////////
//
// Main
//
//////////////////////////////////
int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(600,600);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("Platonic solids in light and shadow");
    glutDisplayFunc(display);               // Set function to draw scene
    glutReshapeFunc(reshape);               // Set function called if window gets resized
    glutMouseFunc(handleMouseClick);        // Set function to handle mouse clicks
    glutMotionFunc(handleMouseMotion);      // Set function to handle mouse motion
    glutKeyboardFunc(handleKeyboardEvent);  // Set function to handle keyboard input
	glutSpecialFunc(handleSpecialKeyEvent); // Set function to handle special key input
    init();
    glutMainLoop();
}
