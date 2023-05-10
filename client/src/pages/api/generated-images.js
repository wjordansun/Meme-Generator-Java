import fs from "fs";
import path from "path";

export default function handler(req, res) {
  const generatedImageFolder = path.join(
    process.cwd(),
    "public/images/generated-images"
  );
  const generatedImages = [];

  try {
    fs.readdirSync(generatedImageFolder).forEach((fileName, index) => {
      generatedImages.push({
        id: index + 1,
        fileName: fileName,
      });
    });
    res.status(200).json({ generatedImages });
  } catch (error) {
    console.log(error);
    res.status(500).json({ message: "Error retrieving generated images" });
  }
}
