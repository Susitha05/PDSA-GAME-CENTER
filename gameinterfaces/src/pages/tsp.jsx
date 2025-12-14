// import React, {useEffect, useState} from "react";
// import City from "../assets/img/citymap.jpg"
// import {HiOutlinePlay, HiPlay, HiRefresh} from "react-icons/hi";
//
// const start_Index = 0;
//
// function TravellingSellsMan(){
//     const [dots, setDots] = useState([]);
//     const [path, setPath] = useState([]);
//     const [userPath, setUserPath] = useState([]);
//     const [isCompleted, setIsCompleted] = useState(false);
//     const [totalDistance, setTotalDistance] = useState(0);
//     const [userName , setUserName] = useState('susitha');
//     const cityNames = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//
//
//     // Load new game
//     const loadNewGame = async () => {
//         const res = await fetch("http://localhost:8081/tsp/new");
//         const data = await res.json();
//         setDots(data.points);
//         setPath([]);
//         setUserPath([start_Index]);
//         setIsCompleted(false);
//         setTotalDistance(0);
//     };
//
//     useEffect(() => {
//         loadNewGame();
//     }, []);
//
//     // Solve TSP via backend
//     const solveTSP = async () => {
//
//         const res = await fetch("http://localhost:8081/tsp/solve", {
//             method: "POST",
//             headers: {"Content-Type": "application/json"},
//             body: JSON.stringify(dots)
//         });
//         const data = await res.json();
//         const orderedDots = data.shortestPath.map(i => dots[i]);
//         setPath(orderedDots);
//         setTotalDistance(data.distance.toFixed(2));
//     };
//
//     // Handle user clicking a dot
//     const handleUserClick = async (i) => {
//         if (isCompleted) return;
//
//         if (!userPath.includes(i)) {
//             const newPath = [...userPath, i];
//             setUserPath(newPath);
//
//             if (newPath.length === dots.length) {
//                 setIsCompleted(true);
//
//                 // Check path via backend
//                 const res = await fetch("http://localhost:8081/tsp/check", {
//                     method: "POST",
//                     headers: {"Content-Type": "application/json"},
//                     body: JSON.stringify({dots, userPath: newPath, user_Name:userName})
//                 });
//                 const data = await res.json();
//                 if (!data.correct) {
//                     alert("Wrong Path! Showing correct path.");
//                 } else {
//                     alert("Correct path!");
//                 }
//
//                 const correctDots = data.correctPath.map(idx => dots[idx]);
//                 setPath(correctDots);
//             }
//         }
//     };
//     const calculateDistance = (p1, p2) => {
//         const dx = p1.x - p2.x;
//         const dy = p1.y - p2.y;
//         return Math.sqrt(dx*dx + dy*dy).toFixed(2); // distance in map % units
//     };
//
//
//     return(
//         <>
//         <div className="w-full bg-green-950 p-4">
//             <label className="ml-2 capitalize text-white text-3xl">Traval Sellsman Problem</label>
//         </div>
//         <div className="w-full h-full">
//             <div className="flex w-full h-[600px] mt-4">
//                 <div
//                     className="relative flex w-2/3 ml-4">
//                     <img src={City} alt="map" className="w-full h-full object-fit"/>
//                     {dots.map((dot, i) => {
//                         const isStart = i === start_Index;
//                         const cityLabel = cityNames[i];
//
//                         return (
//                             <div
//                                 key={i}
//                                 className="absolute cursor-pointer"
//                                 style={{
//                                     left: `${dot.x}%`,
//                                     top: `${dot.y}%`,
//                                     transform: "translate(-50%, -50%)",
//                                 }}
//                                 onClick={() => handleUserClick(i)}
//                             >
//                                 {/* Dot */}
//                                 <div
//                                     className={`w-4 h-4 rounded-full ${
//                                         isStart ? "bg-yellow-400" : "bg-red-600"
//                                     }`}
//                                     title={isStart ? `Start City ${cityLabel}` : `City ${cityLabel}`}
//                                 />
//
//                                 {/* START label */}
//                                 <div className="absolute -top-6 left-1/2 -translate-x-1/2
//                                     bg-white text-black text-xs font-bold px-1 py-0.5 rounded shadow">
//                                     {isStart ? `START (${cityLabel})` : `City ${cityLabel}`}
//                                 </div>
//                             </div>
//                         );
//                     })}
//
//
//                     {userPath.length > 1 && userPath.map((idx, i) => {
//                         if (i === userPath.length - 1) return null;
//                         const p1 = dots[userPath[i]];
//                         const p2 = dots[userPath[i + 1]];
//                         const dist = calculateDistance(p1, p2);
//                         return (
//                             <svg key={"u" + i} className="absolute w-full h-full top-0 left-0 pointer-events-none">
//                                 <line x1={`${p1.x}%`} y1={`${p1.y}%`} x2={`${p2.x}%`} y2={`${p2.y}%`} stroke="blue"
//                                       strokeWidth="3"/>
//                                 <text x={`${(p1.x + p2.x) / 2}%`} y={`${(p1.y + p2.y) / 2}%`} fill="black" fontSize="12"
//                                       textAnchor="middle">
//                                     {dist}
//                                 </text>
//                             </svg>
//                         );
//                     })}
//
//                     {/* Draw TSP path */}
//                     {path.length > 1 &&
//                         path.map((p, i) => {
//                             if (i === path.length - 1) return null;
//                             const p2 = path[i + 1];
//
//                             return (
//                                 <svg
//                                     key={i}
//                                     className="absolute top-0 left-0 w-full h-full pointer-events-none"
//                                 >
//                                     <line
//                                         x1={`${p.x}%`}
//                                         y1={`${p.y}%`}
//                                         x2={`${p2.x}%`}
//                                         y2={`${p2.y}%`}
//                                         stroke="yellow"
//                                         strokeWidth="3"
//                                     />
//                                 </svg>
//                             );
//                         })}
//                 </div>
//                 <div className="ml-8">
//                     <span className="w-full flex justify-center mb-2"><h3
//                         className="font-bold text-[24px] text-amber-600 mb-1"> City Distances</h3></span>
//                     <table className="table-auto text-lg border-collapse border border-gray-400">
//                         <thead>
//                         <tr>
//                             <th className="border px-2">From\To</th>
//                             {dots.map((_, i) => (
//                                 <th key={i} className="border px-2">{cityNames[i]}</th>
//                             ))}
//                         </tr>
//                         </thead>
//                         <tbody>
//                         {dots.map((from, i) => (
//                             <tr key={i}>
//                                 <td className="border px-2 py-3 font-bold">{cityNames[i]}</td>
//                                 {dots.map((to, j) => (
//                                     <td key={j} className="border px-2 text-center">
//                                         {calculateDistance(from, to)}
//                                     </td>
//                                 ))}
//                             </tr>
//                         ))}
//                         </tbody>
//                     </table>
//                     <div className=" mt-10 flex w-full justify-evenly">
//                         <button
//                             className="flex justify-center items-center bg-green-700 p-2 px-8 rounded-2xl gap-2 shadow-lg mr-4"
//                             onClick={solveTSP}
//                         >
//                             <HiPlay className="w-12 h-12"/>
//                             <label className="text-2xl font-semibold text-white">Solve</label>
//                         </button>
//                         <button
//                             className="flex justify-center items-center bg-amber-700 p-2 px-8 rounded-2xl gap-2 shadow-lg"
//                             onClick={loadNewGame}
//                         >
//                             <HiRefresh className="w-12 h-12"/>
//                             <label className="text-2xl font-semibold text-white">Reset</label>
//                         </button>
//                     </div>
//                 </div>
//             </div>
//         </div>
//
//             <div>
//
//             </div>
//
//             <footer className="fixed bottom-0 left-0 w-full bg-green-950 p-2 text-center text-white">
//                 © 2025 Game World
//             </footer>
//         </>
//     )
// }
//
// export default TravellingSellsMan;

import React, { useEffect, useState } from "react";
import City from "../assets/img/citymap.jpg";
import { HiPlay, HiRefresh } from "react-icons/hi";

function TravellingSalesman() {
    const [dots, setDots] = useState([]);
    const [homeCityIndex, setHomeCityIndex] = useState(0);
    const [selectedCities, setSelectedCities] = useState([]);
    const [path, setPath] = useState([]);
    const [userPath, setUserPath] = useState([]);
    const [isCompleted, setIsCompleted] = useState(false);
    const [totalDistance, setTotalDistance] = useState(0);
    const [userName, setUserName] = useState('');
    const [algorithmResults, setAlgorithmResults] = useState(null);
    const [gameStarted, setGameStarted] = useState(false);
    const cityNames = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Load new game
    const loadNewGame = async () => {
        const res = await fetch("http://localhost:8081/tsp/new");
        const data = await res.json();
        setDots(data.points);
        setHomeCityIndex(data.homeCityIndex);
        setSelectedCities([data.homeCityIndex]); // Home city is always selected
        setPath([]);
        setUserPath([]);
        setIsCompleted(false);
        setTotalDistance(0);
        setAlgorithmResults(null);
        setGameStarted(false);
    };

    useEffect(() => {
        loadNewGame();
    }, []);

    // Toggle city selection
    const toggleCitySelection = (index) => {
        if (index === homeCityIndex) return; // Can't deselect home city

        if (selectedCities.includes(index)) {
            setSelectedCities(selectedCities.filter(i => i !== index));
        } else {
            setSelectedCities([...selectedCities, index]);
        }
    };

    // Start the game after city selection
    const startGame = () => {
        if (selectedCities.length < 2) {
            alert("Please select at least 2 cities (including home city)!");
            return;
        }
        if (!userName.trim()) {
            alert("Please enter your name first!");
            return;
        }
        setUserPath([homeCityIndex]);
        setGameStarted(true);
    };

    // Solve TSP with all algorithms
    const solveTSP = async () => {
        if (selectedCities.length < 2) {
            alert("Please select at least 2 cities!");
            return;
        }

        // Get selected cities data
        const selectedCityData = selectedCities.map(idx => dots[idx]);

        const res = await fetch("http://localhost:8081/tsp/solve", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                cities: selectedCityData,
                selectedIndices: selectedCities,
                homeIndex: homeCityIndex
            })
        });
        const data = await res.json();

        setAlgorithmResults(data);

        // Map path indices back to original dot indices
        const orderedDots = data.shortestPath.map(i => selectedCityData[i]);
        setPath(orderedDots);
        setTotalDistance(data.distance.toFixed(2));
    };

    // Handle user clicking a dot
    const handleUserClick = async (i) => {
        if (isCompleted || !gameStarted) return;
        if (!selectedCities.includes(i)) return; // Only allow clicking selected cities

        if (!userPath.includes(i)) {
            const newPath = [...userPath, i];
            setUserPath(newPath);

            // Check if all selected cities are visited
            const allVisited = selectedCities.every(cityIdx => newPath.includes(cityIdx));

            if (allVisited) {
                setIsCompleted(true);

                if (!userName.trim()) {
                    alert("Please enter your name!");
                    return;
                }

                // Get only selected cities
                const selectedCityData = selectedCities.map(idx => dots[idx]);

                // Map user path to selected cities indices
                const mappedUserPath = newPath.map(idx => selectedCities.indexOf(idx));

                const res = await fetch("http://localhost:8081/tsp/check", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        dots: selectedCityData,
                        userPath: mappedUserPath,
                        selectedIndices: selectedCities,
                        homeCityIndex: homeCityIndex,
                        homeCityName: cityNames[homeCityIndex],
                        user_Name: userName
                    })
                });
                const data = await res.json();

                const correctDots = data.correctPath.map(idx => selectedCityData[idx]);
                setPath(correctDots);

                if (data.correct) {
                    alert(`Correct path! 🎉\nYour distance: ${data.userDistance.toFixed(2)} km`);
                } else {
                    alert(`Wrong path!\nYour distance: ${data.userDistance.toFixed(2)} km\nBest distance: ${data.bestDistance.toFixed(2)} km\nAccuracy: ${data.percentage.toFixed(1)}%`);
                }
            }
        }
    };

    const calculateDistance = (p1, p2) => {
        const dx = p1.x - p2.x;
        const dy = p1.y - p2.y;
        return (Math.sqrt(dx * dx + dy * dy) * 10).toFixed(2); // Scale to km
    };

    return (
        <>
            <div className="w-full bg-green-950 p-4">
                <label className="ml-2 capitalize text-white text-3xl">Traveling Salesman Problem</label>
            </div>

            <div className="w-full p-4">
                {/* User Name Input */}
                <div className="mb-4">
                    <label className="text-lg font-semibold mr-2">Player Name:</label>
                    <input
                        type="text"
                        value={userName}
                        onChange={(e) => setUserName(e.target.value)}
                        className="border-2 border-gray-300 rounded px-3 py-1"
                        placeholder="Enter your name"
                        disabled={gameStarted}
                    />
                </div>

                {/* City Selection */}
                {!gameStarted && (
                    <div className="mb-4 p-4 bg-blue-50 rounded">
                        <h3 className="text-xl font-bold mb-2">Select Cities to Visit:</h3>
                        <div className="flex flex-wrap gap-2 mb-3">
                            {dots.map((_, i) => (
                                <button
                                    key={i}
                                    onClick={() => toggleCitySelection(i)}
                                    className={`px-4 py-2 rounded ${
                                        i === homeCityIndex
                                            ? 'bg-yellow-400 font-bold cursor-not-allowed'
                                            : selectedCities.includes(i)
                                                ? 'bg-green-500 text-white'
                                                : 'bg-gray-300 hover:bg-gray-400'
                                    }`}
                                    disabled={i === homeCityIndex}
                                >
                                    City {cityNames[i]} {i === homeCityIndex ? '(HOME)' : ''}
                                </button>
                            ))}
                        </div>
                        <p className="text-sm text-gray-600 mb-2">
                            Selected: {selectedCities.length} cities (minimum 2 required)
                        </p>
                        <button
                            onClick={startGame}
                            className="bg-blue-600 text-white px-6 py-2 rounded font-semibold hover:bg-blue-700"
                        >
                            Start Game
                        </button>
                    </div>
                )}

                <div className="flex w-full gap-4">
                    {/* Map */}
                    <div className="relative w-2/3" style={{ height: '600px' }}>
                        <img src={City} alt="map" className="w-full h-full object-cover rounded" />

                        {dots.map((dot, i) => {
                            const isHome = i === homeCityIndex;
                            const isSelected = selectedCities.includes(i);
                            const cityLabel = cityNames[i];

                            return (
                                <div
                                    key={i}
                                    className="absolute cursor-pointer"
                                    style={{
                                        left: `${dot.x}%`,
                                        top: `${dot.y}%`,
                                        transform: "translate(-50%, -50%)",
                                    }}
                                    onClick={() => handleUserClick(i)}
                                >
                                    <div
                                        className={`w-4 h-4 rounded-full ${
                                            isHome ? "bg-yellow-400" :
                                                isSelected ? "bg-red-600" : "bg-gray-400"
                                        }`}
                                        title={isHome ? `Home City ${cityLabel}` : `City ${cityLabel}`}
                                    />
                                    <div className="absolute -top-6 left-1/2 -translate-x-1/2
                                        bg-white text-black text-xs font-bold px-1 py-0.5 rounded shadow">
                                        {isHome ? `HOME (${cityLabel})` : `${cityLabel}`}
                                    </div>
                                </div>
                            );
                        })}

                        {/* User path lines */}
                        {userPath.length > 1 && userPath.map((idx, i) => {
                            if (i === userPath.length - 1) return null;
                            const p1 = dots[userPath[i]];
                            const p2 = dots[userPath[i + 1]];
                            const dist = calculateDistance(p1, p2);
                            return (
                                <svg key={"u" + i} className="absolute w-full h-full top-0 left-0 pointer-events-none">
                                    <line x1={`${p1.x}%`} y1={`${p1.y}%`} x2={`${p2.x}%`} y2={`${p2.y}%`}
                                          stroke="blue" strokeWidth="3"/>
                                    <text x={`${(p1.x + p2.x) / 2}%`} y={`${(p1.y + p2.y) / 2}%`}
                                          fill="blue" fontSize="12" textAnchor="middle" fontWeight="bold">
                                        {dist} km
                                    </text>
                                </svg>
                            );
                        })}

                        {/* Solution path lines */}
                        {path.length > 1 && path.map((p, i) => {
                            if (i === path.length - 1) return null;
                            const p2 = path[i + 1];
                            const dist = calculateDistance(p, p2);
                            return (
                                <svg key={i} className="absolute top-0 left-0 w-full h-full pointer-events-none">
                                    <line x1={`${p.x}%`} y1={`${p.y}%`} x2={`${p2.x}%`} y2={`${p2.y}%`}
                                          stroke="yellow" strokeWidth="3"/>
                                    <text x={`${(p.x + p2.x) / 2}%`} y={`${(p.y + p2.y) / 2}%`}
                                          fill="yellow" fontSize="12" textAnchor="middle" fontWeight="bold">
                                        {dist} km
                                    </text>
                                </svg>
                            );
                        })}
                    </div>

                    {/* Side Panel */}
                    <div className="w-1/3">
                        <h3 className="font-bold text-2xl text-amber-600 mb-3">Controls</h3>

                        <div className="flex flex-col gap-3 mb-4">
                            <button
                                className="flex items-center justify-center bg-green-700 p-3 rounded-lg gap-2 shadow-lg text-white hover:bg-green-800 disabled:bg-gray-400"
                                onClick={solveTSP}
                                disabled={!gameStarted}
                            >
                                <HiPlay className="w-8 h-8"/>
                                <label className="text-xl font-semibold">Solve with All Algorithms</label>
                            </button>
                            <button
                                className="flex items-center justify-center bg-amber-700 p-3 rounded-lg gap-2 shadow-lg text-white hover:bg-amber-800"
                                onClick={loadNewGame}
                            >
                                <HiRefresh className="w-8 h-8"/>
                                <label className="text-xl font-semibold">New Game</label>
                            </button>
                        </div>

                        {/* Algorithm Results */}
                        {algorithmResults && (
                            <div className="bg-gray-100 p-3 rounded mb-4">
                                <h4 className="font-bold text-lg mb-2">Algorithm Results:</h4>
                                <div className="text-sm space-y-2">
                                    <div className="border-b pb-2">
                                        <p className="font-semibold">1. Nearest Neighbor (Greedy)</p>
                                        <p>Distance: {algorithmResults.algorithm1Distance.toFixed(2)} km</p>
                                        <p>Time: {algorithmResults.algorithm1Time.toFixed(3)} ms</p>
                                    </div>
                                    <div className="border-b pb-2">
                                        <p className="font-semibold">2. Dynamic Programming</p>
                                        <p>Distance: {algorithmResults.algorithm2Distance.toFixed(2)} km</p>
                                        <p>Time: {algorithmResults.algorithm2Time.toFixed(3)} ms</p>
                                    </div>
                                    <div className="border-b pb-2">
                                        <p className="font-semibold">3. Genetic Algorithm</p>
                                        <p>Distance: {algorithmResults.algorithm3Distance.toFixed(2)} km</p>
                                        <p>Time: {algorithmResults.algorithm3Time.toFixed(3)} ms</p>
                                    </div>
                                    <div className="bg-green-200 p-2 rounded mt-2">
                                        <p className="font-bold">Best: {algorithmResults.bestAlgorithm}</p>
                                        <p>Distance: {algorithmResults.distance.toFixed(2)} km</p>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Distance Table */}
                        {gameStarted && selectedCities.length > 0 && (
                            <div className="overflow-auto" style={{ maxHeight: '300px' }}>
                                <h4 className="font-bold text-lg mb-2">Selected Cities Distances (km):</h4>
                                <table className="table-auto text-sm border-collapse border border-gray-400">
                                    <thead>
                                    <tr>
                                        <th className="border px-2 bg-gray-200">From\To</th>
                                        {selectedCities.map((idx) => (
                                            <th key={idx} className="border px-2 bg-gray-200">
                                                {cityNames[idx]}
                                            </th>
                                        ))}
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {selectedCities.map((fromIdx) => (
                                        <tr key={fromIdx}>
                                            <td className="border px-2 py-1 font-bold bg-gray-100">
                                                {cityNames[fromIdx]}
                                            </td>
                                            {selectedCities.map((toIdx) => (
                                                <td key={toIdx} className="border px-2 text-center">
                                                    {fromIdx === toIdx ? '-' : calculateDistance(dots[fromIdx], dots[toIdx])}
                                                </td>
                                            ))}
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            <footer className="fixed bottom-0 left-0 w-full bg-green-950 p-2 text-center text-white">
                © 2025 Game World - Traveling Salesman Problem
            </footer>
        </>
    );
}

export default TravellingSalesman;