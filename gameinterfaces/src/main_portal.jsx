import React, {useEffect} from 'react'
import LOGO from "./assets/img/LOGO.png"
import { motion } from "framer-motion";
import {useNavigate} from "react-router-dom";


function MainPortal(){

    const navigate = useNavigate();
    useEffect(() => {
        const timer = setTimeout(()=>{
            navigate("/gameDisplay")
        },5000)
    }, [navigate]);
    return(
        <div className="flex w-full h-screen bg-green-950 items-center">
            <div className="flex w-full h-full justify-center items-center">
                <motion.div
                    animate={{ scale: [1, 1.1, 1] }}
                    transition={{
                        duration: 2,
                        repeat: Infinity,
                        repeatType: "loop",
                        ease: "easeInOut"
                    }}
                >
                    <img className="w-72 h-72 object-fill" src={LOGO} alt="LOGO"/>
                </motion.div>
            </div>
        </div>
    )
}

export default MainPortal