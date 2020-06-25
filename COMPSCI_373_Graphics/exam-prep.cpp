double dot(Vector3 u, Vector3 v) {
    return u.x * v.x + u.y * v.y + u.z * v.z;
}

Vector3 lerp(Vector3 u, Vector3 v, double t) {
    Vector3 diff = Vector3(v.x-u.x, v.y-u.y, v.z-u.z);
    return Vector3(u.x + (diff.x * t), u.y + (diff.y * t), u.z + (diff.z * t));
}

bool coplanar(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
    // Use first three vectors to find the eqn for plane, then use fourth to check
    Vector3 firstLine = Vector3(b.x-a.x, b.y-a.y, b.z-a.z);
    Vector3 secondLine = Vector3(c.x-a.x, c.y-a.y, c.z-a.z);
    Vector3 normal = cross(firstLine, secondLine);
    if (dot(normal, a) == dot(normal, b) && dot(normal, b) == dot(normal, c) && dot(normal, c) == dot(normal, d)) {
        return true;
    }
    // Check they don't all just lie on the same line lol
    return false;
}

// Given are the vertices
// const int numVertices=6;
// const float vertices[numVertices][2] = {{200,150},{300,200},{300,300},{200,350},{100,300},{100,200}};
// Which calling sequence of these vertices (using glVertex2fv) results in the shape below if we use the OpenGL commands glBegin(GL_TRIANGLES) and glEnd()?
int index[18]={0,1,2,1,2,3,2,3,4,3,4,5,4,5,0,5,0,1};




void drawTriangleTile(Vector3 verticesTriangles[]) {
    glBegin(GL_TRIANGLES);
        CRColor3d(1.0,0.5,0.0);
        CRVertex3d(verticesTriangles[2].x, verticesTriangles[2].y, verticesTriangles[2].z);
        CRVertex3d(verticesTriangles[0].x, verticesTriangles[0].y, verticesTriangles[0].z);
        CRVertex3d(verticesTriangles[1].x,verticesTriangles[1].y, verticesTriangles[1].z);
        
        CRColor3d(0.0,0.0,1.0);
                
        CRVertex3d(verticesTriangles[2].x,verticesTriangles[2].y, verticesTriangles[2].z);
        CRVertex3d(verticesTriangles[1].x, verticesTriangles[1].y, verticesTriangles[1].z);
        CRVertex3d(verticesTriangles[3].x, verticesTriangles[3].y, verticesTriangles[3].z);
        
        CRVertex3d(verticesTriangles[2].x,verticesTriangles[2].y, verticesTriangles[2].z);
        CRVertex3d(verticesTriangles[3].x, verticesTriangles[3].y, verticesTriangles[3].z);
        CRVertex3d(verticesTriangles[4].x, verticesTriangles[4].y, verticesTriangles[4].z);
        
        CRVertex3d(verticesTriangles[2].x,verticesTriangles[2].y, verticesTriangles[2].z);
        CRVertex3d(verticesTriangles[4].x, verticesTriangles[4].y, verticesTriangles[4].z);
        CRVertex3d(verticesTriangles[0].x, verticesTriangles[0].y, verticesTriangles[0].z);


        

    glEnd();
}




void drawMesh(Vector3 vertices[], Vector3 colors[], int indices[], int numIndices) {
    glBegin(GL_TRIANGLES);
    for (int i = 0; i < numIndices; i++) {
        int idx = indices[i];
        CRColor3d(colors[idx].x, colors[idx].y, colors[idx].z);
        CRVertex3d(vertices[idx].x, vertices[idx].y, vertices[idx].z);
    }
    glEnd();
}

Colour complement(Colour c) {
    return Colour(1 - c.r, 1 - c.g, 1 - c.b);
}


Vector2 coordinates(CIEColour c) {
    double total = c.X + c.Y + c.Z;
    return Vector2(c.X / total, c.Y / total);
}



SpectralFunction resultantSDF(SpectralFunction light, SpectralFunction surface) {
    return SpectralFunction(light.r * surface.r, light.g * surface.g, light.b * surface.b);
}

red = ambientIntensityRed * ambientReflecCoefRed;
green = ambientIntensityGreen * ambientReflecCoefGreen;
blue = ambientIntensityBlue * ambientReflecCoefBlue ;

double phongSingleColour(double Ia, double Id, double Is,double Pa, double Pd, double Ps,double alpha,double kc, double kl, double kq, Vector3 pointOnSurface, Vector3 m, Vector3 lightPosition, Vector3 viewPoint) {
    // Calc ambient amt
    double ambient = Ia * Pa;
    
    // Calc diffuse amt
    Vector3 s = Vector3(lightPosition.x - pointOnSurface.x, lightPosition.y - pointOnSurface.y, lightPosition.z - pointOnSurface.z);
    double d = s.magnitude();
    Vector3 v = Vector3(viewPoint.x - pointOnSurface.x, viewPoint.y - pointOnSurface.y, viewPoint.z - pointOnSurface.z);
    double diffuse = Id * Pd * dot(s, m)/(s.magnitude() * m.magnitude());
    
    // Calc specular amt
    Vector3 h = (s.normalized() + v.normalized()).normalized();
    double specular = Is * Ps * pow(dot(h, m)/(h.magnitude() * m.magnitude()), alpha);
    
    double denominator = kc + kl * d + kq * pow(d, 2);
    return ambient + (diffuse + specular)/denominator;
}


void drawPixel(int i, int j) 
{
	pointOnSurface = p(i,j);
	Vector3 surfaceNormal = n0 * ((1 - (i/120.0)) * (1 - (j/120.0))) + n1 * ((1 - (i/120.0)) * ((j/120.0)))+ n2 * (((i/120.0)) * (1 - (j/120.0)))+ n3 * (((i/120.0)) * ((j/120.0)));
	double c = phongSingleColour(ambientIntensity, diffuseIntensity, specularIntensity,ambientReflecCoef, diffuseReflecCoef, specularReflecCoef, shininess, kc, kl, kq, pointOnSurface, surfaceNormal, lightPosition, viewPoint);
	glColor3d(c, c, c);
	glVertex3d(pointOnSurface.x, pointOnSurface.y, pointOnSurface.z);
}


glRotatef(90,1,0,0);
glTranslatef(0,1,0);


glRotatef(90,1,0,0);
glScalef(2,1,1);


glRotatef(90,1,0,0);
glScalef(2,1,1);


glRotatef(90,1,0,0);
glScalef(2,1,1);


glRotatef(90,1,0,0);
glScalef(2,1,1);

A=5;
B=3;
C=0;


Vector3 surfaceNormal(Vector3 p0, Vector3 p1, Vector3 p2) {
    return cross(p1-p0, p2-p0).normalized();
}

Vector3 transformCoordinateSystem(Vector3 point, Vector3 u, Vector3 v, Vector3 n, Vector3 uvnOrigin, Vector3 a, Vector3 b, Vector3 c, Vector3 abcOrigin) {
    Vector3 r = Vector3(point.x * u.x, point.x * u.y, point.x * u.z) + Vector3(point.y * v.x, point.y * v.y, point.y * v.z) + Vector3(point.z * n.x, point.z * n.y, point.z * n.z) + uvnOrigin;
    return Vector3(dot(r, a), dot(r, b), dot(r,c)) + abcOrigin;
}



void drawPyramid(Vector3 pyramidVertices[]) {
    CRColor3d(0.0, 0.0, 1.0);
    glBegin(GL_TRIANGLES);
    for (int i=0; i < 4; i++) {
        CRVertex3d(pyramidVertices[4].x, pyramidVertices[4].y, pyramidVertices[4].z);
        CRVertex3d(pyramidVertices[(i+1)%4].x, pyramidVertices[(i+1)%4].y, pyramidVertices[(i+1)%4].z);
        CRVertex3d(pyramidVertices[(i+2)%4].x, pyramidVertices[(i+2)%4].y, pyramidVertices[(i+2)%4].z);
        if (i == 0) {
            CRColor3d(1.0, 0.0, 0.0);
        }
    }
    glEnd();
}


double phongDirectionalSource(double Ia, double Id, double Is, double Pa, double Pd, double Ps, double alpha, Vector3 pointOnSurface, Vector3 m, Vector3 s, Vector3 viewPoint) {
    // Calc ambient amt
    double ambient = Ia * Pa;
    
    // Calc diffuse amt
    s.x *= -1;
    s.y *= -1;
    s.z *= -1;
    double d = s.magnitude();
    Vector3 v = Vector3(viewPoint.x - pointOnSurface.x, viewPoint.y - pointOnSurface.y, viewPoint.z - pointOnSurface.z);
    double diffuse = Id * Pd * dot(s, m)/(s.magnitude() * m.magnitude());
    
    // Calc specular amt
    Vector3 h = (s.normalized() + v.normalized()).normalized();
    double specular = Is * Ps * pow(dot(h, m)/(h.magnitude() * m.magnitude()), alpha);
    
    return ambient + (diffuse + specular);
}


static bool side(Vector3 p, Vector3 v0, Vector3 v1) {
    float expected = (p.x-v0.x)*(v1.y-v0.y)-(p.y-v0.y)*(v1.x-v0.x);
    return expected <= 0;
}
bool pixelInShape(Vector3 vertices[], int vertexCount, Vector3 point) {
    for (int i=0; i<vertexCount-1; i++) {
        Vector3 pt1 = vertices[0];
        Vector3 pt2 = vertices[i+1];
        Vector3 pt3 = vertices[i+2];
        
        if (side(point, pt1, pt2) == side(point, pt2, pt3) and side(point, pt2, pt3) == side(point, pt3, pt1)) {
            return true;
        }
    }
    
    return false;
}


glRotatef(-90,0,0,1);
glRotatef(-90,0,1,0);
glScalef(1,2,1);



_glScalef(2,1,1);

_glPushMatrix();

_glRotatef(-90,0,0,1);	

drawHouse();

_glPopMatrix();

_glTranslatef(-1,0,0);

drawHouse();


s1 = 0; t1 = 0.5;
s2 = 0; t2 = 1;
s3 = 0.25; t3 = 0.5;
s4 = 0.25; t4 = 1;
s5 = 0.5; t5 = 0.5;
s6 = 0.5; t6 = 1;
s7 = 0.75; t7 = 0.5;
s8 = 0.75; t8 = 1;
s9 = 1; t9 = 0.5;
s10 = 1; t10 = 1;


for(int r=0; r<windowHeight; r++) {
  for(int c=0; c<windowWidth; c++) {

    // construct ray through (c, r) using u,v,n and H,W

    //  Please ONLY MODIFY the values for dx, dy, dz

    dx = -n.x*N + (((2.0*c)/(windowWidth-1))-1)*u.x*W + (((2.0*r)/(windowHeight-1))-1)*v.x*H;

    dy = -n.y*N + (((2.0*c)/(windowWidth-1))-1)*u.y*W + (((2.0*r)/(windowHeight-1))-1)*v.y*H;

    dz = -1;

    Vector d = Vector(dx, dy, dz);

    // intersect ray with scene objects

    Hit hit = intersect(eye, d);

    // shade pixel accordingly

    Color color = shade(hit);

    glColor3f(color.r, color.g, color.b);

    glVertex2f((GLfloat)c, (GLfloat)r);

  }

}



double Plane::Intersect(Vector source, Vector d)
{
    /*====================================================*/
    /* == Enter ray/plane intersection calculations here ==*/
    /* == Delete the line "t = -1.0;" and insert your solution instead ==*/
    /* == NOTE 1: If there is no ray-plane intersection set t = -1.0; ==*/
    /*====================================================*/
	float t = (a-source.Dot(n))/(d.Dot(n));
	if (t < 0) {
	    return -1.0;
	}
    return t;
}


#include <iostream>
double Sphere::Intersect(Vector source, Vector d)
{
	source= (source - translation)/scaling;
	d = d/scaling;

	float A = d.Dot(d);
	float B = 2.0*(source.Dot(d));
	float C = source.Dot(source) - 1;

	float t; // the parameter t for the closest intersection point of ray with the sphere. If no intersection t=-1.0
    
    if ((B*B - 4.0*A*C) < 0) {
        return -1.0;
    }
    
	float t1 = (-1.0 * B + sqrt(B*B - 4.0*A*C))/(2.0*A);
	float t2 = (-1.0 * B - sqrt(B*B - 4.0*A*C))/(2.0*A);

	if (t1 < t2 && t1 > 0.0) {
	    t = t1;
	} else {
	    t = t2;
	}
	if (t < 0.0) {
	    return -1.0;
	}

	return t;
}

Vector Sphere::Normal(Vector p)
{
    Vector s=(p-translation)/scaling;
    s=s/scaling;
    return s.Normalize();
}


CVec3df c(float t){ 

     return CVec3df(0+2*t, 3-3*t, 4-4*t);

}


CVec3df c(float t){ 

     return CVec3df(0+2*t, 3-3*t, 4-4*t);

}


CVec3df c(float t){ 

     return CVec3df(0+2*t, 3-3*t, 4-4*t);

}


CVec3df c(float t){ 

    if (t > 0.5) {
        // Do curve here
        return CVec3df(-2.0*cos(Pi*t)+1,0, 2*sin(Pi*t)+1);
    } else {
        // straight line here
        return CVec3df(2*t, 0, 2+2*t);
    }
}


CVec3df c(float t){ 

    if (t > 0.5) {
        // Do curve here
        return CVec3df(-2.0*cos(Pi*t)+1,0, 2*sin(Pi*t)+1);
    } else {
        // straight line here
        return CVec3df(2*t, 0, 2+2*t);
    }
}


double Basis1(double t){ return -t*t +1;} // B1(t) 
double Basis2(double t){ return -t*t +t;} // B2(t) 
double Basis3(double t){ return t*t;} // B3(t) 

CVec3df c(float t, CVec3df p0, CVec3df r0, CVec3df p1)
{
	CVec3df p=Basis1(t)*p0+Basis2(t)*r0+Basis3(t)*p1;
	return p;
}

