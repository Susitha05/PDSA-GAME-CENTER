import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import LOGO from "./assets/img/LOGO.png"

function mainPortal(){
    const navigate = useNavigate();

    useEffect(() => {
        // Auto-navigate to game display after 2 seconds
        const timer = setTimeout(() => {
            navigate('/gameDisplay');
        }, 2000);

        return () => clearTimeout(timer);
    }, [navigate]);

    const handleLogoClick = () => {
        navigate('/gameDisplay');
    };

    return(
        <div className="flex w-full h-screen bg-gradient-to-br from-green-950 via-emerald-900 to-teal-950 items-center">
            <div className="flex flex-col w-full h-full justify-center items-center gap-8">
                <img 
                    className="w-72 h-72 object-fill cursor-pointer hover:scale-110 transition-transform duration-300" 
                    src={LOGO} 
                    alt="LOGO"
                    onClick={handleLogoClick}
                />
                <div className="text-center">
                    <h1 className="text-4xl font-bold text-white mb-4">PDSA Game Center</h1>
                    <p className="text-xl text-emerald-200 mb-8">Master Algorithms Through Play</p>
                    <button 
                        onClick={handleLogoClick}
                        className="px-8 py-3 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-semibold rounded-lg transition-all transform hover:scale-105 shadow-lg"
                    >
                        Enter Game Center
                    </button>
                    <p className="text-sm text-emerald-300 mt-4 animate-pulse">Redirecting in 2 seconds...</p>
                </div>
            </div>
        </div>
    )
}
export default mainPortal