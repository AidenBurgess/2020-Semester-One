import sys
import time

from matplotlib import pyplot
from matplotlib.patches import Rectangle

# this is our module that performs the reading of a png image
# it is part of the skeleton code
import imageIO.png


# this function reads an RGB color png file and returns width, height, as well as pixel arrays for r,g,b
def readRGBImageToSeparatePixelArrays(input_filename):

    image_reader = imageIO.png.Reader(filename=input_filename)
    # png reader gives us width and height, as well as RGB data in image_rows (a list of rows of RGB triplets)
    (image_width, image_height, rgb_image_rows, rgb_image_info) = image_reader.read()

    print("read image width={}, height={}".format(image_width, image_height))

    # our pixel arrays are lists of lists, where each inner list stores one row of greyscale pixels
    pixel_array_r = []
    pixel_array_g = []
    pixel_array_b = []

    for row in rgb_image_rows:
        pixel_row_r = []
        pixel_row_g = []
        pixel_row_b = []
        r = 0
        g = 0
        b = 0
        for elem in range(len(row)):
            # RGB triplets are stored consecutively in image_rows
            if elem % 3 == 0:
                r = row[elem]
            elif elem % 3 == 1:
                g = row[elem]
            else:
                b = row[elem]
                pixel_row_r.append(r)
                pixel_row_g.append(g)
                pixel_row_b.append(b)

        pixel_array_r.append(pixel_row_r)
        pixel_array_g.append(pixel_row_g)
        pixel_array_b.append(pixel_row_b)

    return (image_width, image_height, pixel_array_r, pixel_array_g, pixel_array_b)


# This method takes a greyscale pixel array and writes it into a png file
def writeGreyscalePixelArraytoPNG(output_filename, pixel_array, image_width, image_height):
    # now write the pixel array as a greyscale png
    file = open(output_filename, 'wb')  # binary mode is important
    writer = imageIO.png.Writer(image_width, image_height, greyscale=True)
    writer.write(file, pixel_array)
    file.close()


# Creates a two dimensional array representing an image as a very simple (not very efficient) list of lists
# datastructure.
# The outer list is covering all the image rows. Each row is an inner list covering the columns of the image.
def createInitializedGreyscalePixelArray(image_width, image_height, initValue = 0):
    new_array = []
    for row in range(image_height):
        new_row = []
        for col in range(image_width):
            new_row.append(initValue)
        new_array.append(new_row)

    return new_array


# Takes as input a greyscale pixel array and computes the minimum and maximum greyvalue.
# Returns minimum and maximum greyvalue as a tuple
def computeMinAndMaxValues(pixel_array, image_width, image_height):
    min_value = sys.maxsize
    max_value = -min_value

    for y in range(image_height):
        for x in range(image_width):
            if pixel_array[y][x] < min_value:
                min_value = pixel_array[y][x]
            if pixel_array[y][x] > max_value:
                max_value = pixel_array[y][x]

    return(min_value, max_value)


# This function analyzes the return value of the connected component label algorithm to derive the 
# bounding box around the largest connected component. Thus, it prepares the result to be shown 
# using a rectangle around the detected barcode.
def determineLargestConnectedComponent(cclabeled, label_size_dictionary, image_width, image_height):

    final_labeled = createInitializedGreyscalePixelArray(image_width, image_height)

    size_of_largest_component = 0
    label_of_largest_component = 0
    for lbl_i in label_size_dictionary.keys():
        if label_size_dictionary[lbl_i] > size_of_largest_component:
            size_of_largest_component = label_size_dictionary[lbl_i]
            label_of_largest_component = lbl_i

    print("label of largest component: ", label_of_largest_component)

    # determine bounding box of the largest component only
    bbox_min_x = image_width
    bbox_min_y = image_height
    bbox_max_x = 0
    bbox_max_y = 0
    for y in range(image_height):
        for x in range(image_width):
            if cclabeled[y][x] == label_of_largest_component:
                final_labeled[y][x] = 255
                if x < bbox_min_x:
                    bbox_min_x = x
                if y < bbox_min_y:
                    bbox_min_y = y
                if x > bbox_max_x:
                    bbox_max_x = x
                if y > bbox_max_y:
                    bbox_max_y = y
            else:
                final_labeled[y][x] = 0

    return (final_labeled, (bbox_min_x, bbox_max_x, bbox_min_y, bbox_max_y))


# a simple Queue datastructure based on a list, not very efficient but sufficient
# for a simple connected component labeling implementation
class Queue:
    def __init__(self):
        self.items = []

    def isEmpty(self):
        return self.items == []

    def enqueue(self, item):
        self.items.insert(0,item)

    def dequeue(self):
        return self.items.pop()

    def size(self):
        return len(self.items)
        


# TO BE IMPLEMENTED by students (see coderunner)

# Week 1

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
    
    return new



# Week 2

# computes vertical edges using Sobel filter (see lecture slides)
# we ignore border pixels
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
    return new_image

# computes horizontal edges using Sobel filter (see lecture slides)
# we ignore border pixels
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
    return new_image


# takes vertical and horizontal edges as input and subtracts horizontal from vertical edges
# additionally, if this subtraction is negative, the value is set to 0
# assumes that vertical and horizontal edges are normalized!
# returns the subtracted image
def computeStrongVerticalEdgesBySubtractingHorizontal(vertical_edges, horizontal_edges, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    for j in range(image_height):
        for i in range(image_width):
            new_image[j][i] = max(0, vertical_edges[j][i] - horizontal_edges[j][i])
            
            
    return new_image

def computeBoxAveraging3x3(pixel_array, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    for i in range(2, image_height-2):
        for j in range(2, image_width-2):
            val = 0
            for x in range(-2, 3):
                for y in range(-2,3):
                    val += pixel_array[i+y][j+x] * 1/16
            new_image[i+x][j+y] = abs(val)
    return new_image


# returns 255 for pixels greater or equal (GE) threshold value, 0 otherwise (strictly lower)
def computeThresholdGE(pixel_array, threshold_value, image_width, image_height):
    new_image = createInitializedGreyscalePixelArray(image_width, image_height)
    for i in range(image_height):
        for j in range(image_width):
            new_image[i][j] = 255 if pixel_array[i][j] >= threshold_value else 0
    return new_image


# Week 3

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
    


# This is our code skeleton that performs the barcode detection.
# The code works on images of items where the barcode is shown in a horizontal way.
# Feel free to try it on your own images of objects, but make sure that the input image size and barcode size
# is not too different from our examples.
def main():
    total_time = time.time()
    filename = "./images/barcodeDetection/barcode_02.png"

    # we read in the png file, and receive three pixel arrays for red, green and blue components, respectively
    # each pixel array contains 8 bit integer values between 0 and 255 encoding the color values
    start_time = time.time()
    (image_width, image_height, px_array_r, px_array_g, px_array_b) = readRGBImageToSeparatePixelArrays(filename)   
    print("readRGBImageToSeparatePixelArrays: %s seconds" % (time.time() - start_time))

    # first we have to convert the red, green and blue pixel arrays to a greyscale representation.
    # This is done using the formula: greyvalue = 0.299 * red + 0.587 * green + 0.114 * blue
    # TODO: implement this conversion function
    start_time = time.time()
    px_array = computeRGBToGreyscale(px_array_r, px_array_g, px_array_b, image_width, image_height)
    print("computeRGBToGreyscale: %s seconds" % (time.time() - start_time))

    # next we make sure that the input greyscale image is scaled across the full 8 bit range (0 and 255)
    # TODO: implement this contrast stretching function
    start_time = time.time()
    px_array = scaleTo0And255AndQuantize(px_array, image_width, image_height)
    print("scaleTo0And255AndQuantize: %s seconds" % (time.time() - start_time))


    # setup the plots for intermediate results in a figure
    fig1, axs1 = pyplot.subplots(3, 2)
    axs1[0, 0].set_title('Input greyscale image')
    axs1[0, 0].imshow(px_array, cmap='gray')

    # now we compute the horizontal edges in the image and take its absolute values...
    # TODO: implement this edge enhancement function
    start_time = time.time()
    horizontal_edges = computeHorizontalEdgesSobelAbsolute(px_array, image_width, image_height)
    print("computeHorizontalEdgesSobelAbsolute: %s seconds" % (time.time() - start_time))
    # scale horizontal edges to the range 0 and 255
    start_time = time.time()
    horizontal_edges = scaleTo0And255AndQuantize(horizontal_edges, image_width, image_height)
    print("scaleTo0And255AndQuantize: %s seconds" % (time.time() - start_time))

    # as well as the vertical edges in the image, again taking its absolute values.
    # TODO: implement this edge enhancement function
    start_time = time.time()
    vertical_edges = computeVerticalEdgesSobelAbsolute(px_array, image_width, image_height)
    print("computeVerticalEdgesSobelAbsolute: %s seconds" % (time.time() - start_time))
    # scale vertical edges to the range 0 and 255
    start_time = time.time()
    vertical_edges = scaleTo0And255AndQuantize(vertical_edges, image_width, image_height)
    print("scaleTo0And255AndQuantize: %s seconds" % (time.time() - start_time))

    # now we want to enhance strong vertical edges (our barcodes) by subtracting all horizontal edges
    # TODO: implement this edge processing function
    start_time = time.time()
    edges = computeStrongVerticalEdgesBySubtractingHorizontal(vertical_edges, horizontal_edges, image_width, image_height)
    print("computeStrongVerticalEdgesBySubtractingHorizontal: %s seconds" % (time.time() - start_time))

    start_time = time.time()
    edges = scaleTo0And255AndQuantize(edges, image_width, image_height)
    print("scaleTo0And255AndQuantize: %s seconds" % (time.time() - start_time))

    # next we blur our edge image using a 3x3 mean filter (averaging or box filter) a total of four times
    # the result of the 3x3 mean filter ignores the border pixels, therefore the output is 0 along the image border
    start_time = time.time()
    averaged_edges = edges
    for i in range(2):
        averaged_edges = computeBoxAveraging3x3(averaged_edges, image_width, image_height)
    averaged_edges = scaleTo0And255AndQuantize(averaged_edges, image_width, image_height)
    print("computeBoxAveraging3x3: %s seconds" % (time.time() - start_time))

    axs1[0, 1].set_title('Averaged edge image')
    axs1[0, 1].imshow(averaged_edges, cmap='gray')

    # we use a threshold value of 70 to binarize the edge image. Note that this threshold depends crucially
    # on the fact that we are always working with normalized 8 bit images between 0 and 255
    threshold_value = 70
    # TODO: implement this thresholding function
    start_time = time.time()
    thresholded = computeThresholdGE(averaged_edges, threshold_value, image_width, image_height)
    print("computeThresholdGE: %s seconds" % (time.time() - start_time))

    axs1[1, 0].set_title('Thresholded image')
    axs1[1, 0].imshow(thresholded, cmap='gray')
    start_time = time.time()
    eroded = thresholded
    for i in range(4):
        # TODO: implement this morphological operation
        eroded = computeErosion8Nbh3x3FlatSE(eroded, image_width, image_height)
    print("computeErosion8Nbh3x3FlatSE: %s seconds" % (time.time() - start_time))
    dilated = eroded
    start_time = time.time()
    for i in range(4):
        # TODO: implement this morphological operation
        dilated = computeDilation8Nbh3x3FlatSE(dilated, image_width, image_height)
    print("computeDilation8Nbh3x3FlatSE: %s seconds" % (time.time() - start_time))

    axs1[1, 1].set_title('Morphologically processed image')
    axs1[1, 1].imshow(dilated, cmap='gray')


    # taking the morphologically cleaned up binary image, we finally look for the largest connected component
    # in the image
    # TODO: implement connected component labeling
    start_time = time.time()
    (cclabeled, size_dict_cc) = computeConnectedComponentLabeling(dilated, image_width, image_height)
    print("computeConnectedComponentLabeling: %s seconds" % (time.time() - start_time))


    # inspect the result of the connected component labeling, derive the largest component and its bounding box
    (final_labeled, (bbox_min_x, bbox_max_x, bbox_min_y, bbox_max_y)) = \
        determineLargestConnectedComponent(cclabeled, size_dict_cc, image_width, image_height)

    axs1[2, 0].set_title('Largest detected component')
    axs1[2, 0].imshow(final_labeled, cmap='gray')

    print("bbox {} {} {} {}".format(bbox_min_x, bbox_max_x, bbox_min_y, bbox_max_y))


    # Draw the bounding box as a rectangle into the original input image
    axs1[2, 1].set_title('Final image of detection')
    axs1[2, 1].imshow(px_array, cmap='gray')
    rect = Rectangle((bbox_min_x, bbox_min_y), bbox_max_x - bbox_min_x, bbox_max_y - bbox_min_y, linewidth=3,
                     edgecolor='g', facecolor='none')
    axs1[2, 1].add_patch(rect)

    print("Total_time: %s seconds" % (time.time() - total_time))
    # plot the current figure
    pyplot.show()

if __name__ == "__main__":
    main()

