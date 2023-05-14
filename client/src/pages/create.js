import { useState, useEffect } from "react";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import Image from "next/image";
import Header from "@/components/header";

export default function create() {
  const [selectedImage, setSelectedImage] = useState(null);
  const [topText, setTopText] = useState("");
  const [bottomText, setBottomText] = useState("");
  const [fileName, setFileName] = useState("");
  const [selectedFormat, setSelectedFormat] = useState("top-bottom");
  const [requestedImages, setRequestedImages] = useState([]);

  const handleImageClick = (image) => {
    setSelectedImage(image);
  };

  const handleModalClose = () => {
    setSelectedImage(null);
    setTopText("");
    setBottomText("");
    setFileName("");
  };

  const handleTopTextChange = (e) => {
    setTopText(e.target.value);
  };

  const handleBottomTextChange = (e) => {
    setBottomText(e.target.value);
  };

  const handleFileNameChange = (e) => {
    setFileName(e.target.value);
  };

  const handleFormatChange = (e) => {
    setSelectedFormat(e.target.value);
  };

  useEffect(() => {
    // Function to retrieve all image names from the backend
    const getAllImages = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/getAllTemplates"
        );
        const data = response.data;
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
          `http://localhost:8080/templates/${imageName}`,
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
            baseImage: imageName,
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

  const handleGenerateMeme = async (e) => {
    e.preventDefault();
    if (topText.length && fileName.length) {
      const formData = new FormData();
      if (selectedImage.memeTemplate === "user-upload") {
        formData.append("baseImage", selectedImage.file);
        formData.append("selectedFormat", selectedFormat);
      } else {
        formData.append("baseImage", selectedImage.baseImage);
      }
      formData.append("topText", topText);
      if (bottomText !== "") formData.append("bottomText", bottomText);
      formData.append("fileName", fileName);

      // if(selectedFormat !== "") {
      //   try {
      //     const res = await axios.post(
      //       "http://localhost:8080/generateMeme/" + selectedImage.selectedFormat,
      //       formData,
      //       {
      //         headers: {
      //           "Content-Type": "multipart/form-data",
      //         },
      //       }
      //     );
      //     console.log(res.data);
      //     alert(fileName + " created successfully!");
      //   } catch (error) {
      //     console.log(error);
      //   }
      // }
      try {
        const res = await axios.post(
          "http://localhost:8080/generateMeme/" + selectedFormat,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        console.log(res.data);
        alert(fileName + " created successfully!");
      } catch (error) {
        console.log(error);
      }
    }
    setSelectedImage(null);
    setTopText("");
    setBottomText("");
    setFileName("");
    setSelectedFormat("");
  };

  const handleUploadImage = (e) => {
    const file = e.target.files[0];
    const reader = new FileReader();

    reader.onload = () => {
      setSelectedImage({
        id: Date.now(),
        src: reader.result,
        baseImage: file.name,
        memeTemplate: "user-upload",
        file: file,
      });
    };

    if (file) {
      reader.readAsDataURL(file);
    }
  };

  return (
    <main className="container mx-auto px-4">
      <Header />
      <h1 className="text-center text-3xl font-bold mb-8">Templates</h1>
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
              <Image src={image.src} layout="fill" objectFit="cover" />
            </div>
          </li>
        ))}
      </ul>
      {selectedImage && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white rounded-lg px-8 py-6">
            <div
              className="relative w-full h-0"
              style={{ paddingBottom: "66.67%" }}
            >
              <Image
                src={selectedImage.src}
                layout="fill"
                objectFit="contain"
              />
              <div
                className="absolute top-0 left-0 w-full h-1/2 flex items-center justify-center text-white font-bold text-2xl"
                style={{ textShadow: "1px 1px #000" }}
              >
                {topText}
              </div>
              {selectedFormat !== "top" && (
                <div
                  className="absolute bottom-0 left-0 w-full h-1/2 flex items-center justify-center text-white font-bold text-2xl"
                  style={{ textShadow: "1px 1px #000" }}
                >
                  {bottomText}
                </div>
              )}
            </div>

            <form className="mt-4">
              <div className="mb-4">
                <label className="block text-gray-700 font-medium mb-2">
                  Choose Format
                </label>
                <select
                  className="block w-full rounded border-gray-400 shadow-sm py-2 px-3 leading-tight focus:outline-none focus:border-blue-500 text-black"
                  value={selectedFormat}
                  onChange={handleFormatChange}
                >
                  <option value="top-bottom">top-bottom</option>
                  <option value="two-right">two-right</option>
                  <option value="top">top</option>
                </select>
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 font-medium mb-2">
                  Top Text
                </label>
                <input
                  type="text"
                  className="block w-full rounded border-gray-400 shadow-sm py-2 px-3 leading-tight focus:outline-none focus:border-blue-500 text-black"
                  value={topText}
                  onChange={handleTopTextChange}
                />
              </div>
              {selectedFormat !== "top" && (
                <div className="mb-4">
                  <label className="block text-gray-700 font-medium mb-2">
                    Bottom Text
                  </label>
                  <input
                    type="text"
                    className="block w-full rounded border-gray-400 shadow-sm py-2 px-3 leading-tight focus:outline-none focus:border-blue-500 text-black"
                    value={bottomText}
                    onChange={handleBottomTextChange}
                  />
                </div>
              )}
              <div className="mb-4">
                <label className="block text-gray-700 font-medium mb-2">
                  File Name
                </label>
                <input
                  type="text"
                  className="block w-full rounded border-gray-400 shadow-sm py-2 px-3 leading-tight focus:outline-none focus:border-blue-500 text-black"
                  value={fileName}
                  onChange={handleFileNameChange}
                />
              </div>
              <button
                type="button"
                className="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600"
                onClick={handleModalClose}
              >
                Close
              </button>
              <button
                type="button"
                className="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600"
                onClick={handleGenerateMeme}
              >
                Generate
              </button>
            </form>
          </div>
        </div>
      )}
      <div className="mt-8">
        <h2 className="text-2xl font-bold mb-2">Upload Image (1 MB Max)</h2>
        <input
          type="file"
          accept="image/jpeg, image/png"
          className="mb-4"
          onChange={handleUploadImage}
        />
      </div>
    </main>
  );
}
