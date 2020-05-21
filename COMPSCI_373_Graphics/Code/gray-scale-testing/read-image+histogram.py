from matplotlib import pyplot
import imageio as io
import numpy as np
from PIL import Image


def main():
    filename = "images/gray-1.png"
    img = Image.open(filename)
    im_array = np.asarray(img)

    pyplot.imshow(im_array)
    
if __name__ == "__main__":
    main()