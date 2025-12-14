// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import SnakeLadder from './SnakeLadder';

import MainPortal from './main_portal'
import GameDisplay from './pages/game_display'
import Tsp from './pages/tsp'
import TowerOfHanoiBase from "./pages/TowerOfHanoiBase";
function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
          <Route path="/snake-ladder" element={<SnakeLadder />}/>
            <Route path="/gameDisplay" element={<GameDisplay />}/>
            <Route path="/tspGame" element={<Tsp />}/>
            <Route path="/towerOfHanoi" element={<TowerOfHanoiBase />}/>
            
        </Routes>
      </BrowserRouter>
  )
}

export default App;
