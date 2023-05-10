import Link from "next/link";

export default function Header() {
  return (
    <header className="container mx-auto px-4 py-4">
      <h1 className="text-center text-4xl font-extrabold text-blue-500 tracking-wider">
        Meme Generator
      </h1>
      <nav className="flex justify-center mt-4">
        <Link href="/">
          <span className="mr-4 text-blue-500 hover:text-blue-700">
            Create Meme
          </span>
        </Link>
        <Link href="/view">
          <span className="mr-4 text-blue-500 hover:text-blue-700">
            Generated Images
          </span>
        </Link>
      </nav>
    </header>
  );
}
