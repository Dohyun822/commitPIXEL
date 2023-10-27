"use client";
import { useDispatch } from "react-redux";
import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav";
import Sidebar from "./sidebar/sidebar";
import { setTool } from "@/store/slices/toolSlice";

const Browser = () => {
  const dispatch = useDispatch();
  const handleKeyDown = (e: React.KeyboardEvent) => {
    console.log(e.key);
    if (e.key === 'h' || e.key === 'H') {
      dispatch(setTool("panning"));
    } else if (e.key === 'b' || e.key === 'B') {
      dispatch(setTool("painting"));
    } else if (e.key === 'i' || e.key === 'I') {
      dispatch(setTool("copying"));
    } else if (e.key === "Escape") {
      dispatch(setTool(null));
    }
  };
  return (
    <main onKeyDown={handleKeyDown} className="w-screen h-screen bg-bgColor">
          <Nav />
          <div className="grid grid-cols-4 w-full h-[92%] place-items-center">
            <CanvasContainer />
            <Sidebar />
          </div>
    </main>
  );
}

export default Browser;