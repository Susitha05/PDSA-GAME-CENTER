import React, {useEffect, useState} from "react";
import City from "../assets/img/citymap.jpg"
import {HiOutlinePlay, HiPlay, HiRefresh} from "react-icons/hi";

function TravellingSellsMan(){
    const [dots,setDots] = useState([])
    const [path,setpath] = useState([])
    const [totalDistance,setdistance] = useState(0)

    useEffect(() => {
        const pts = Array.from({length:8}).map(()=>({
            x: Math.random() * 90 + 5,
                y: Math.random() * 90 + 5,
        }));
        setDots(pts)
    }, []);

    const distance = (a,b)=>{
        const dx =a.x -b.x;
        const dy = a.y - b.y
        return Math.sqrt(dx * dx + dy * dy)
    }
    const solveTSP = () => {
        if (dots.length === 0) return;

        const visited = new Set();
        let current = dots[0]; // start point
        let order = [current];
        visited.add(dots.indexOf(current));

        while (visited.size < dots.length) {
            let nextDot = null;
            let shortest = Infinity;

            dots.forEach((d, idx) => {
                if (!visited.has(idx)) {
                    const dist = distance(current, d);
                    if (dist < shortest) {
                        shortest = dist;
                        nextDot = d;
                    }
                }
            });

            order.push(nextDot);
            visited.add(dots.indexOf(nextDot));
            current = nextDot;
        }

        // return to start for full loop
        order.push(order[0]);

        // calculate total distance
        let total = 0;
        for (let i = 0; i < order.length - 1; i++) {
            total += distance(order[i], order[i + 1]);
        }

        setpath(order);
        setdistance(total.toFixed(2));
        console.log(total.toFixed(2));
    };

    return(
        <>
        <div className="w-full bg-green-950 p-4">
            <label className="ml-2 capitalize text-white text-3xl">Traval Sellsman Problem</label>
        </div>
        <div className="w-full h-full">
            <div className="flex w-full h-[600px] mt-4">
                <div
                    className="relative flex w-1/2 ml-4"
                    style={{
                        backgroundImage: `url(${City})`,
                        backgroundSize: "cover",
                        backgroundPosition: "center",
                    }}
                >
                    {dots.map((dot, i) => (
                        <div
                            key={i}
                            className="absolute w-4 h-4 bg-red-600 rounded-full"
                            style={{
                                left: `${dot.x}%`,
                                top: `${dot.y}%`,
                                transform: "translate(-50%, -50%)",
                            }}
                        />
                    ))}

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
                        window.location.reload()
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