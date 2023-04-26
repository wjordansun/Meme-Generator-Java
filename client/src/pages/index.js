import { useState } from "react";
import axios from "axios";
import Image from "next/image";
import hotdog from "../../templates/hotdog.jpg";
import spongebob from "../../templates/spongebob.jpg";

const images = [
  {
    id: 1,
    src: hotdog,
    baseImage: "hotdog.jpg",
    memeTemplate : "top-bottom"
  },
  {
    id: 2,
    src: spongebob,
    baseImage: "spongebob.jpg",
    memeTemplate : "top-bottom"
  },
  {
    id: 3,
    src: drakememe,
    baseImage: "drakememe.jpg",
    memeTemplate: "two-right"
  },
  {
    id: 4,
    src: "https://via.placeholder.com/200x200",
  },
];

export default function Home() {
  const [selectedImage, setSelectedImage] = useState(null);
  const [topText, setTopText] = useState("");
  const [bottomText, setBottomText] = useState("");
  const [fileName, setFileName] = useState("");

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

  const handleGenerateMeme = async (e) => {
    e.preventDefault();
    if (topText.length && bottomText.length && fileName.length) {
      const formData = new FormData();

      formData.append("templateName", selectedImage.template);
      formData.append("topText", topText);
      formData.append("bottomText", bottomText);
      formData.append("fileName", fileName);

      try {
        const res = await axios.post(
          "http://localhost:8080/generateMeme",
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
  };

  return (
    <main className="container mx-auto px-4">
      <ul className="flex flex-wrap -mx-4">
        {images.map((image) => (
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
              <div
                className="absolute bottom-0 left-0 w-full h-1/2 flex items-center justify-center text-white font-bold text-2xl"
                style={{ textShadow: "1px 1px #000" }}
              >
                {bottomText}
              </div>
            </div>

            <form className="mt-4">
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
    </main>
  );
}
