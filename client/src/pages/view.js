import { useEffect, useState } from "react";
import Image from "next/image";
import axios from "axios";
import { v4 as uuidv4 } from "uuid";
import Header from "@/components/header";
import { sendError } from "next/dist/server/api-utils";

export default function view() {
  const [requestedImages, setRequestedImages] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);

  // useEffect(() => {
  //   const fetchGeneratedImages = async () => {
  //     try {
  //       const response = await fetch("/api/generated-images");
  //       const data = await response.json();
  //       setGeneratedImages(data.generatedImages);
  //     } catch (error) {
  //       console.log(error);
  //     }
  //   };

  //   fetchGeneratedImages();
  // }, []);
  useEffect(() => {
    // Function to retrieve all image names from the backend
    const getAllImages = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/getAllGeneratedImages"
        );
        const data = response.data;
        console.log("data");
        console.log(data);
        // Call the function to fetch each image
        for (const key in data) {
          getImage(data[key]);
        }
      } catch (error) {
        console.error("Error:", error);
      }
    };

    // Function to fetch a single image
    const getImage = async (imageName) => {
      try {
        const response = await axios.get(
          `http://localhost:8080/generatedImages/${imageName}`,
          {
            responseType: "blob",
          }
        );
        const blob = response.data;

        // Create a temporary URL for the image
        const imageURL = URL.createObjectURL(blob);

        // Add the image URL to the requestedImages array
        setRequestedImages((prevImages) => [
          ...prevImages,
          {
            id: uuidv4(),
            src: imageURL,
            fileName: imageName,
          },
        ]);
      } catch (error) {
        console.error("Error:", error);
      }
    };

    // Call the function to retrieve all image names on component mount
    setRequestedImages([]);
    getAllImages();
  }, []); // Empty dependency array ensures the effect runs only once on mount

  const handleImageClick = (image) => {
    setSelectedImage(image);
  };

  const handleModalClose = () => {
    setSelectedImage(null);
  };

  const handleDownloadImage = () => {
    const downloadLink = document.createElement("a");
    downloadLink.href = selectedImage.src;
    downloadLink.download = selectedImage.fileName;
    downloadLink.click();
  };

  return (
    <main className="container mx-auto px-4">
      <Header />
      <h1 className="text-center text-3xl font-bold mb-8">Generated Images</h1>
      <ul className="flex flex-wrap -mx-4">
        {requestedImages.map((image) => (
          <li
            key={image.id}
            className="w-full md:w-1/2 lg:w-1/3 px-4 mb-8 cursor-pointer"
            onClick={() => handleImageClick(image)}
          >
            <div
              className="relative w-full h-0"
              style={{ paddingBottom: "66.67%" }}
            >
              <Image
                src={image.src}
                alt={image.fileName}
                layout="fill"
                objectFit="cover"
              />
            </div>
          </li>
        ))}
      </ul>
      {selectedImage && (
        <div className="fixed inset-0 flex items-center justify-center">
          <div className="bg-white rounded-lg p-4 w-3/4 h-3/4">
            <div className="relative w-full h-full">
              <Image
                src={selectedImage.src}
                alt={selectedImage.fileName}
                layout="fill"
                objectFit="contain"
              />
            </div>
            <div className="flex justify-center mt-4">
              <button
                type="button"
                className="bg-blue-500 text-white py-2 px-4 rounded mr-4"
                onClick={handleModalClose}
              >
                Close
              </button>
              <button
                type="button"
                className="bg-green-500 text-white py-2 px-4 rounded"
                onClick={handleDownloadImage}
              >
                Download
              </button>
            </div>
          </div>
        </div>
      )}
    </main>
  );
}
