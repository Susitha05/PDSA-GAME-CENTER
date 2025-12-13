import React, {useEffect, useState} from "react";
import City from "../assets/img/citymap.jpg"
import {HiOutlinePlay, HiPlay, HiRefresh} from "react-icons/hi";

function TravellingSellsMan(){
    const [dots, setDots] = useState([]);
    const [path, setPath] = useState([]);
    const [userPath, setUserPath] = useState([]);
    const [isCompleted, setIsCompleted] = useState(false);
    const [totalDistance, setTotalDistance] = useState(0);

    // Load new game
    const loadNewGame = async () => {
        const res = await fetch("http://localhost:8080/tsp/new");
        const data = await res.json();
        setDots(data.points);
        setPath([]);
        setUserPath([]);
        setIsCompleted(false);
        setTotalDistance(0);
    };

    useEffect(() => {
        loadNewGame();
    }, []);

    // Solve TSP via backend
    const solveTSP = async () => {
        const res = await fetch("http://localhost:8080/tsp/solve", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(dots)
        });
        const data = await res.json();
        const orderedDots = data.shortestPath.map(i => dots[i]);
        setPath(orderedDots);
        setTotalDistance(data.distance.toFixed(2));
    };

    // Handle user clicking a dot
    const handleUserClick = async (i) => {
        if (isCompleted) return;
        if (!userPath.includes(i)) {
            const newPath = [...userPath, i];
            setUserPath(newPath);

            if (newPath.length === dots.length) {
                setIsCompleted(true);

                // Check path via backend
                const res = await fetch("http://localhost:8080/tsp/check", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({dots, userPath: newPath})
                });
                const data = await res.json();
                if (!data.correct) {
                    alert("Wrong Path! Showing correct path.");
                } else {
                    alert("Correct path!");
                }

                const correctDots = data.correctPath.map(idx => dots[idx]);
                setPath(correctDots);
            }
        }
    };

    return(
        <>
        <div className="w-full bg-green-950 p-4">
            <label className="ml-2 capitalize text-white text-3xl">Traval Sellsman Problem</label>
        </div>
        <div className="w-full h-full">
            <div className="flex w-full h-[600px] mt-4">
                <div
                    className="relative flex w-1/2 ml-4">
                    <img src={City} alt="map" className="w-full h-full object-cover"/>
                    {dots.map((dot, i) => (
                        <div
                            key={i}
                            className="absolute w-4 h-4 bg-red-600 rounded-full"
                            style={{
                                left: `${dot.x}%`,
                                top: `${dot.y}%`,
                                transform: "translate(-50%, -50%)",
                            }}
                            onClick={()=>handleUserClick(i)}
                        />
                    ))}

                    {userPath.length > 1 && userPath.map((idx,i)=>{
                        if(i === userPath.length-1) return null;
                        const p1 = dots[userPath[i]];
                        const p2 = dots[userPath[i+1]];
                        return (
                            <svg key={"u"+i} className="absolute w-full h-full top-0 left-0 pointer-events-none">
                                <line x1={`${p1.x}%`} y1={`${p1.y}%`} x2={`${p2.x}%`} y2={`${p2.y}%`} stroke="blue" strokeWidth="3"/>
                            </svg>
                        );
                    })}

                    {/* Draw TSP path */}
                    {path.length > 1 &&
                        path.map((p, i) => {
                            if (i === path.length - 1) return null;
                            const p2 = path[i + 1];

                            return (
                                <svg
                                    key={i}
                                    className="absolute top-0 left-0 w-full h-full pointer-events-none"
                                >
                                    <line
                                        x1={`${p.x}%`}
                                        y1={`${p.y}%`}
                                        x2={`${p2.x}%`}
                                        y2={`${p2.y}%`}
                                        stroke="yellow"
                                        strokeWidth="3"
                                    />
                                </svg>
                            );
                        })}
                </div>
                <div className=" w-1/3 h-full p-4 shadow-lg border-2 rounded-lg ml-10">
                    <div className="flex flex-col gap-2 w-full h-36 justify-center shadow-lg items-center bg-gray-200">
                        <label className="text-[36px] text-amber-700 font-semibold block">0%</label>
                        <label className="text-[30px] font-bold text-green-600 block">High Score</label>
                    </div>
                    <div className="mt-4 flex flex-col justify-center items-center">
                       <span
                           className="flex justify-center p-2 border-b-4 border-black rounded-t-2xl w-full bg-gray-100">
                           <label className="text-[20px] font-bold">Record Histroy</label>
                       </span>
                        <span className="flex bg-gray-100 w-full">
                                NO DATA
                        </span>
                    </div>
                </div>
            </div>
        </div>
            <div className="flex w-1/2 h-autoflex justify-center mt-4">
                <button
                    className="flex justify-center items-center bg-green-700 p-2 px-8 ml-4 rounded-2xl gap-2 shadow-lg"
                    onClick={solveTSP}
                >
                    <HiPlay className="w-12 h-12"/>
                    <label className="text-2xl font-semibold text-white">Solve</label>
                </button>
                <button
                    className="flex justify-center items-center bg-amber-700 p-2 px-8 ml-4 rounded-2xl gap-2 shadow-lg"
                    onClick={()=>{
                        loadNewGame()
                    }}
                >
                    <HiRefresh className="w-12 h-12"/>
                    <label className="text-2xl font-semibold text-white">Reset</label>
                </button>
            </div>
        </>
    )
}

export default TravellingSellsMan;