import { useState } from "react";
import Image from "next/image";

const images = [
	{
		id: 1,
		src: "https://via.placeholder.com/200x200",
	},
	{
		id: 2,
		src: "https://via.placeholder.com/200x200",
	},
	{
		id: 3,
		src: "https://via.placeholder.com/200x200",
	},
	{
		id: 4,
		src: "https://via.placeholder.com/200x200",
	},
	{
		id: 5,
		src: "https://via.placeholder.com/200x200",
	},
];

export default function Home() {
	const [selectedImage, setSelectedImage] = useState(null);
	const [topText, setTopText] = useState("");
	const [bottomText, setBottomText] = useState("");

	const handleImageClick = (image) => {
		setSelectedImage(image);
	};

	const handleModalClose = () => {
		setSelectedImage(null);
		setTopText("");
		setBottomText("");
	};

	const handleTopTextChange = (e) => {
		setTopText(e.target.value);
	};

	const handleBottomTextChange = (e) => {
		setBottomText(e.target.value);
	};

	return (
		<main className='container mx-auto px-4'>
			<ul className='flex flex-wrap -mx-4'>
				{images.map((image) => (
					<li
						key={image.id}
						className='w-full md:w-1/2 lg:w-1/3 px-4 mb-8 cursor-pointer'
						onClick={() => handleImageClick(image)}
					>
						<div
							className='relative w-full h-0'
							style={{ paddingBottom: "66.67%" }}
						>
							<Image src={image.src} layout='fill' objectFit='cover' />
						</div>
					</li>
				))}
			</ul>
			{selectedImage && (
				<div className='fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center'>
					<div className='bg-white rounded-lg px-8 py-6'>
						<div
							className='relative w-full h-0'
							style={{ paddingBottom: "66.67%" }}
						>
							<Image
								src={selectedImage.src}
								layout='fill'
								objectFit='contain'
							/>
							<div
								className='absolute top-0 left-0 w-full h-1/2 flex items-center justify-center text-white font-bold text-2xl'
								style={{ textShadow: "1px 1px #000" }}
							>
								{topText}
							</div>
							<div
								className='absolute bottom-0 left-0 w-full h-1/2 flex items-center justify-center text-white font-bold text-2xl'
								style={{ textShadow: "1px 1px #000" }}
							>
								{bottomText}
							</div>
						</div>

						<form className='mt-4'>
							<div className='mb-4'>
								<label className='block text-gray-700 font-medium mb-2'>
									Top Text
								</label>
								<input
									type='text'
									className='block w-full rounded border-gray-400 shadow-sm py-2 px-3 leading-tight focus:outline-none focus:border-blue-500 text-black'
									value={topText}
									onChange={handleTopTextChange}
								/>
							</div>
							<div className='mb-4'>
								<label className='block text-gray-700 font-medium mb-2'>
									Bottom Text
								</label>
								<input
									type='text'
									className='block w-full rounded border-gray-400 shadow-sm py-2 px-3 leading-tight focus:outline-none focus:border-blue-500 text-black'
									value={bottomText}
									onChange={handleBottomTextChange}
								/>
							</div>
							<button
								type='button'
								className='bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600'
								onClick={handleModalClose}
							>
								Close
							</button>
						</form>
					</div>
				</div>
			)}
		</main>
	);
}
