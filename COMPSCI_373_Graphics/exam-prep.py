
def computeHistogram(pixel_array, image_width, image_height, nr_bins = 256):
    h = [0.0] * nr_bins
    for line in pixel_array:
        for pixel in line:
            h[pixel] += 1.0
    return h

def computeCumulativeHistogram(pixel_array, image_width, image_height, nr_bins = 256):
    H = [0.0] * nr_bins
    for line in pixel_array:
        for pixel in line:
            H[pixel] += 1.0
    
    C = [0.0] * nr_bins
    total = 0.0
    for i, val in enumerate(H):
        total += val
        C[i] = total
    return C


def computeRGBToGreyscale(pixel_array_r, pixel_array_g, pixel_array_b, image_width, image_height):
    
    greyscale_pixel_array = createInitializedGreyscalePixelArray(image_width, image_height)
    
    # STUDENT CODE HERE
    for i in range(image_height):
        for j in range(image_width):
            greyscale_pixel_array[i][j] =  round(0.299*pixel_array_r[i][j] + 0.587*pixel_array_g[i][j] + 0.114*pixel_array_b[i][j])
    
    return greyscale_pixel_array

def scaleTo0And255AndQuantize(pixel_array, image_width, image_height):
    new = createInitializedGreyscalePixelArray(image_width, image_height)
    
    f_lo, f_hi = computeMinAndMaxValues(pixel_array, image_width, image_height)
    if f_lo == f_hi:
        return new
    
    limit = lambda n, minn, maxn: max(min(maxn, n), minn)
    
    for i in range(image_height):
        for j in range(image_width):
            pixel = pixel_array[i][j]
            s_out = round((pixel - f_lo) * (255/(f_hi-f_lo)))
            new[i][j] = limit(s_out, 0, 255)
    
    # print(f_lo, f_hi)
    return new

def computeVerticalEdgesSobelAbsolute(pixel_array, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    filter_matrix = {
        (-1, -1): -0.125, (0, 1): 0, (1, -1): 0.125,
        (-1, 0): -0.25, (0, 0): 0, (1, 0): 0.25,
        (-1, 1): -0.125, (0, -1): 0, (1, 1): 0.125
    }
    for i in range(1, image_height-1):
        for j in range(1, image_width-1):
            val = 0
            for x,y in filter_matrix:
                val += pixel_array[i+y][j+x] * filter_matrix[(x,y)]
            new_image[i][j] = abs(val)
    # print(new_image)
    return new_image

def computeHorizontalEdgesSobelAbsolute(pixel_array, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    filter_matrix = {
        (-1, -1): 0.125, (0, -1): 0.25, (1, -1): 0.125,
        (-1, 0): 0, (0, 0): 0, (1, 0): 0,
        (-1, 1): -0.125, (0, 1): -0.25, (1, 1): -0.125
    }
    for i in range(1, image_height-1):
        for j in range(1, image_width-1):
            val = 0
            for x,y in filter_matrix:
                val += pixel_array[i+y][j+x] * filter_matrix[(x,y)]
            new_image[i][j] = abs(val)
    # print(new_image)
    return new_image


def computeStrongVerticalEdgesBySubtractingHorizontal(vertical_edges, horizontal_edges, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    for j in range(image_height):
        for i in range(image_width):
            new_image[j][i] = max(0, vertical_edges[j][i] - horizontal_edges[j][i])
            
            
    return new_image


def computeStrongVerticalEdgesBySubtractingHorizontal(vertical_edges, horizontal_edges, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    for j in range(image_height):
        for i in range(image_width):
            new_image[j][i] = max(0, vertical_edges[j][i] - horizontal_edges[j][i])
            
            
    return new_image


def computeThresholdGE(pixel_array, threshold_value, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    for i in range(image_height):
        for j in range(image_width):
            new_image[i][j] = 255 if pixel_array[i][j] >= threshold_value else 0
    # print(new_image)
    return new_image


def computeErosion8Nbh3x3FlatSE(pixel_array, image_width, image_height):
    new = [[0 for i in range(image_width)] for j in range(image_height)]
    
    for i in range(1, image_height-1):
        for j in range(1, image_width-1):
            all_ones = True
            for x in range(-1, 2):
                for y in range(-1, 2):
                    if pixel_array[i+x][j+y] == 0:
                        all_ones = False
            new[i][j] = 1 if all_ones else 0
            
    return new


def computeDilation8Nbh3x3FlatSE(pixel_array, image_width, image_height):
    new = [[0 for i in range(image_width)] for j in range(image_height)]
    
    for i in range(image_height):
        for j in range(image_width):
            all_zeros = True
            for x in range(-1, 2):
                for y in range(-1, 2):
                    if not(0<i+x<image_height) or not(0<j+y<image_width):
                        continue
                    if pixel_array[i+x][j+y] > 0:
                        all_zeros = False
            new[i][j] = 0 if all_zeros else 1
            
    return new


def computeConnectedComponentLabeling(pixel_array, image_width, image_height):
    currentLabel = 1
    visited = set()
    ccl = [[0 for i in range(image_width)] for j in range(image_height)]
    
    for i in range(image_height):
        for j in range(image_width):
            val = pixel_array[i][j]
            if (val != 0) and ((i, j) not in visited):
                q = Queue()
                q.enqueue((i, j))
                visited.add((i,j))
                count = 0
                while q.size() != 0:
                    x, y = q.dequeue()
                    ccl[x][y] = currentLabel
                    # Left
                    if (0<=y-1) and (pixel_array[x][y-1]!=0) and ((x, y-1) not in visited):
                        q.enqueue((x,y-1))
                        visited.add((x,y-1))
                    # Right
                    if (y+1<image_width) and (pixel_array[x][y+1]!=0) and ((x, y+1) not in visited):
                        q.enqueue((x,y+1))
                        visited.add((x,y+1))
                    # Upper
                    if (0<=x-1) and (pixel_array[x-1][y]!=0) and ((x-1, y) not in visited):
                        q.enqueue((x-1,y))
                        visited.add((x-1,y))
                    # Lower
                    if (x+1<image_height) and (pixel_array[x+1][y]!=0) and ((x+1, y) not in visited):
                        q.enqueue((x+1,y))
                        visited.add((x+1,y))
                
                currentLabel +=1

    # Count number of pixels in each component
    counts = {}
    for i in range(1, currentLabel):
        count = sum(x.count(i) for x in ccl)
        counts[i] = count
    
    return ccl, counts
    