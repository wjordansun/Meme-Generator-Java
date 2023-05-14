import { useState } from "react";
import axios from "axios";
import Image from "next/image";
import Header from "../components/header";
import Create from "./create";

export default function Home() {
  return (
    <main className="container mx-auto px-4">
      <Create />
    </main>
  );
}
