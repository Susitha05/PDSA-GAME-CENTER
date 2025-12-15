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

import React, {useEffect, useState} from "react";
import City from "../assets/img/citymap.jpg";
import {HiPlay, HiRefresh, HiOutlineX, HiOutlineUser, HiOutlineUserCircle,HiOutlineInformationCircle} from "react-icons/hi";


const NameDialog = ({open, onClose, onSubmit}) => {
    const [userName, setUserName] = useState('');

    if (!open) return null;

    const handleSubmit = () => {
        if (userName.trim()) {
            onSubmit(userName);
            onClose();
        } else {
            alert('Please enter your name!');
        }
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
            <div className="bg-white rounded-lg p-6 shadow-xl max-w-md w-full">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-bold">Enter Your Name</h2>
                    <button
                        className="text-gray-500 hover:text-gray-700"
                        onClick={onClose}
                    >
                        <HiOutlineX className="w-6 h-6"/>
                    </button>
                </div>

                <div className="mb-4">
                    <label className="text-lg font-semibold block mb-2">Player Name:</label>
                    <input
                        type="text"
                        value={userName}
                        onChange={(e) => setUserName(e.target.value)}
                        className="border-2 border-gray-300 rounded px-3 py-2 w-full"
                        placeholder="Enter your name"
                        onKeyPress={(e) => {
                            if (e.key === 'Enter') {
                                handleSubmit();
                            }
                        }}
                        autoFocus
                    />
                </div>

                <div className="flex gap-2">
                    <button
                        onClick={handleSubmit}
                        className="flex-1 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                    >
                        Continue
                    </button>
                    <button
                        onClick={onClose}
                        className="flex-1 bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400"
                    >
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
};


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
    const [namebox, setnamebox] = useState(false)
    const [scoreCard, setscoreCard] = useState([])
    const cityNames = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Load new game
    const loadNewGame = async () => {
        const res = await fetch("http://localhost:8085/tsp/new");
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
    useEffect(() => {
        if (!userName) setnamebox(true)
    }, [])

    useEffect(() => {
        if (!userName) return;

        const fetchScores = async () => {
            try {
                const res = await fetch(`http://localhost:8085/tsp/${userName}`);
                const data = await res.json();
                setscoreCard(data.scores);
            } catch (err) {
                console.error(err);
            }
        }

        fetchScores();
    }, [userName]);

    // Toggle city selection
    const toggleCitySelection = (index) => {
        if (index === homeCityIndex) return;

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

        const res = await fetch("http://localhost:8085/tsp/solve", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
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
        setTotalDistance(data.distance);
    };
    const handleNameSubmit = (name) => {
        setUserName(name);
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

                const res = await fetch("http://localhost:8085/tsp/check", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
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
            <div style={{backgroundColor: "#0C2B4E"}} className="flex w-full p-4 justify-between border-b-[0.5px] border-gray-100/50">
                <label className="ml-2 capitalize text-white text-3xl font-semibold">Traveling Salesman Problem</label>
                <span className="flex items-center text-xl gap-2 text-white mr-2">
                    <HiOutlineUserCircle className="w-8 h-8"/>
                    <label className="font-semibold">Player Name: {!userName ? "player 1" : userName}</label>
                </span>
            </div>

            <div style={{backgroundColor: "#0C2B4E"}} className=" w-screen h-full pb-10 p-4">
                {/* City Selection */}
                {!gameStarted && (
                    <div className="mb-4 p-2 bg-blue-50/20 rounded-lg shadow-2xl ">
                        <h3 style={{color: "#F4F4F4"}} className="text-xl font-bold mb-2">Select Cities to Visit:</h3>
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
                            <button
                                onClick={startGame}
                                className="ml-4 bg-blue-600 text-white px-6 py-2 rounded font-semibold hover:bg-blue-700"
                            >
                                Start Game
                            </button>
                        </div>
                        <p className="text-base font-semibold text-amber-700 mb-2">
                            Selected: {selectedCities.length} cities (minimum 2 required)
                        </p>
                    </div>
                )}

                <div className="flex w-full gap-4">
                    {/* Map */}
                    <div className="relative w-2/3" style={{height: '600px'}}>
                        <img src={City} alt="map" className="w-full h-full object-cover rounded"/>

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
                    <div className="w-1/3 bg-white/20 rounded-2xl shadow-2xl p-2">
                        <div>

                        </div>

                        {gameStarted && selectedCities.length > 0 ?(
                            <div className="overflow-auto" style={{  MaxHeight: '500px'}}>
                                <span className="flex w-full justify-center"><h4 className="font-bold text-white text-lg mb-2">Selected Cities Distances (km):</h4></span>
                                <table className="table-auto text-lg border-collapse border border-gray-400">
                                    <thead>
                                    <tr>
                                        <th className="border  bg-gray-200 text-lg">From\To</th>
                                        {selectedCities.map((idx) => (
                                            <th key={idx} className="border px-2 bg-gray-200 text-lg">
                                                {cityNames[idx]}
                                            </th>
                                        ))}
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {selectedCities.map((fromIdx) => (
                                        <tr key={fromIdx}>
                                            <td className="border px-2 py-1 font-bold bg-gray-100  text-lg">
                                                {cityNames[fromIdx]}
                                            </td>
                                            {selectedCities.map((toIdx) => (
                                                <td key={toIdx} className="border px-2 text-center  text-lg">
                                                    {fromIdx === toIdx ? '-' : calculateDistance(dots[fromIdx], dots[toIdx])}
                                                </td>
                                            ))}
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        ):(
                            <div className="flex justify-center">
                                <div className="flex justify-center mt-2 w-[550px] h-[400px] rounded-2xl shadow-2xl shadow-black/50 bg-gray-100/20">
                                    <span className="flex flex-col justify-center items-center text-lg">
                                        <HiOutlineInformationCircle className="w-10 h-10 text-red-900"/>
                                        <label className="font-semibold text-red-900">Start Game to Procced </label>
                                    </span>
                                </div>

                            </div>
                        )}
                        <div className="flex flex-col gap-3 mt-4 mb-4">
                            <button
                                className="flex items-center shadow-2xl justify-center bg-green-700 p-3 rounded-lg gap-2 text-white hover:bg-green-800 disabled:bg-gray-400"
                                onClick={solveTSP}
                                disabled={!gameStarted}
                            >
                                <HiPlay className="w-8 h-8"/>
                                <label className="text-xl font-semibold">Solve with All Algorithms</label>
                            </button>
                            <button
                                className="flex items-center justify-center bg-amber-700 p-3 rounded-lg gap-2 shadow-2xl text-white hover:bg-amber-800"
                                onClick={loadNewGame}
                            >
                                <HiRefresh className="w-8 h-8"/>
                                <label className="text-xl font-semibold">New Game</label>
                            </button>
                        </div>

                        {/* Algorithm Results */}
                        {/*{algorithmResults && (*/}
                        {/*    <div className="bg-gray-100 p-3 rounded mb-4">*/}
                        {/*        <h4 className="font-bold text-lg mb-2">Algorithm Results:</h4>*/}
                        {/*        <div className="text-sm space-y-2">*/}
                        {/*            <div className="border-b pb-2">*/}
                        {/*                <p className="font-semibold">1. Nearest Neighbor (Greedy)</p>*/}
                        {/*                <p>Distance: {algorithmResults.algorithm1Distance} km</p>*/}
                        {/*                <p>Time: {algorithmResults.algorithm1Time} ms</p>*/}
                        {/*            </div>*/}
                        {/*            <div className="border-b pb-2">*/}
                        {/*                <p className="font-semibold">2. Dynamic Programming</p>*/}
                        {/*                <p>Distance: {algorithmResults.algorithm2Distance} km</p>*/}
                        {/*                <p>Time: {algorithmResults.algorithm2Time} ms</p>*/}
                        {/*            </div>*/}
                        {/*            <div className="border-b pb-2">*/}
                        {/*                <p className="font-semibold">3. Genetic Algorithm</p>*/}
                        {/*                <p>Distance: {algorithmResults.algorithm3Distance} km</p>*/}
                        {/*                <p>Time: {algorithmResults.algorithm3Time} ms</p>*/}
                        {/*            </div>*/}
                        {/*            <div className="bg-green-200 p-2 rounded mt-2">*/}
                        {/*                <p className="font-bold">Best: {algorithmResults.bestAlgorithm}</p>*/}
                        {/*                <p>Distance: {algorithmResults.distance} km</p>*/}
                        {/*            </div>*/}
                        {/*        </div>*/}
                        {/*    </div>*/}
                        {/*)}*/}
                    </div>

                </div>
                {gameStarted&&(
                <div className="w-full p-4 mt-4 bg-gray-100/20 rounded-2xl shadow-xl">
                        <>
                        <span className="text-2xl font-bold text-white mb-4">User Score Board</span>
                            <div className="w-full flex justify-between gap-4">
                        <div className="flex w-4/6 mt-4">
                            <table className="table-auto border-2 border-gray-300 w-full text-center text-lg">
                                <thead className="bg-gray-100 border-2">
                                <th className="border px-2 py-1 font-bold bg-gray-100 text-lg">ID</th>
                                <th className="border px-2 py-1 font-bold bg-gray-100 text-lg">Name</th>
                                <th className="border px-2 py-1 font-bold bg-gray-100 text-lg">Score</th>
                                <th className="border px-2 py-1 font-bold bg-gray-100 text-lg">Total Distence</th>
                                </thead>
                                <tbody>
                                {scoreCard.map((score) => (
                                    <tr key={score.id} className="bg-gray-300/20">
                                        <td className="border px-2 text-center  text-lg">{score.id}</td>
                                        <td  className="border px-2 text-center  text-lg">{score.name}</td>
                                        <td  className="border px-2 text-center  text-lg">{score.accuracyDistance}</td>
                                        <td  className="border px-2 text-center  text-lg">{score.totalDistance}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>

                            {algorithmResults && (
                                <div className="bg-gray-100 p-3 rounded mt-4 w-2/6">
                                    <h4 className="font-bold text-lg mb-2">Algorithm Results:</h4>
                                    <div className="text-sm space-y-2">
                                        <div className="border-b pb-2">
                                            <p className="font-semibold">1. Nearest Neighbor (Greedy)</p>
                                            <p>Distance: {algorithmResults.algorithm1Distance} km</p>
                                            <p>Time: {algorithmResults.algorithm1Time} ms</p>
                                        </div>
                                        <div className="border-b pb-2">
                                            <p className="font-semibold">2. Dynamic Programming</p>
                                            <p>Distance: {algorithmResults.algorithm2Distance} km</p>
                                            <p>Time: {algorithmResults.algorithm2Time} ms</p>
                                        </div>
                                        <div className="border-b pb-2">
                                            <p className="font-semibold">3. Genetic Algorithm</p>
                                            <p>Distance: {algorithmResults.algorithm3Distance} km</p>
                                            <p>Time: {algorithmResults.algorithm3Time} ms</p>
                                        </div>
                                        <div className="bg-green-200 p-2 rounded mt-2">
                                            <p className="font-bold">Best: {algorithmResults.bestAlgorithm}</p>
                                            <p>Distance: {algorithmResults.distance} km</p>
                                        </div>
                                    </div>
                                </div>
                            )}
                            </div>
                        </>
                </div>
                )}
            </div>

            <footer style={{backgroundColor: "#0C2B4E"}}
                    className="fixed bottom-0 left-0 w-full p-2 text-center border-t-[0.5px] border-gray-100/20 text-white">
                © 2025 Game World - Traveling Salesman Problem
            </footer>
            <NameDialog
                open={namebox}
                onClose={() => setnamebox(false)}
                onSubmit={handleNameSubmit}
            />
        </>
    );
}

export default TravellingSalesman;

