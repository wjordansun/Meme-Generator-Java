import { useState } from "react";
import axios from "axios";
import Image from "next/image";
import hotdog from "../../templates/hotdog.jpg";
import spongebob from "../../templates/spongebob.jpg";
import drakememe from "../../templates/drakememe.jpg";
import society from "../../templates/society.jpg";
import Header from "../components/header";
import Create from "./create";

export default function Home() {
  return (
    <main className="container mx-auto px-4">
      <Create />
    </main>
  );
}
