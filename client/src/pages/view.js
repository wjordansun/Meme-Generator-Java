import { useEffect, useState } from "react";
import Image from "next/image";
import Header from "@/components/header";

export default function view() {
  const [generatedImages, setGeneratedImages] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);

  useEffect(() => {
    const fetchGeneratedImages = async () => {
      try {
        const response = await fetch("/api/generated-images");
        const data = await response.json();
        setGeneratedImages(data.generatedImages);
      } catch (error) {
        console.log(error);
      }
    };

    fetchGeneratedImages();
  }, []);

  const handleImageClick = (image) => {
    setSelectedImage(image);
  };

  const handleModalClose = () => {
    setSelectedImage(null);
  };

  const handleDownloadImage = () => {
    const downloadLink = document.createElement("a");
    downloadLink.href = `/images/generated-images/${selectedImage.fileName}`;
    downloadLink.download = selectedImage.fileName;
    downloadLink.click();
  };

  return (
    <main className="container mx-auto px-4">
      <Header />
      <h1 className="text-center text-3xl font-bold mb-8">Generated Images</h1>
      <ul className="flex flex-wrap -mx-4">
        {generatedImages.map((image) => (
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
                src={`/images/generated-images/${image.fileName}`}
                layout="fill"
                objectFit="cover"
              />
            </div>
          </li>
        ))}
      </ul>
      {selectedImage && (
        <div className="fixed inset-0 flex items-center justify-center">
          <div className="bg-white rounded-lg p-4 w-1/3 h-1/3">
            <img
              src={`/images/generated-images/${selectedImage.fileName}`}
              alt={selectedImage.fileName}
              className="object-contain max-w-full max-h-full mx-auto"
            />
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
