// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import MainPortal from './main_portal'
import GameDisplay from './pages/game_display'
import Tsp from './pages/tsp'
import Traffic from './components/PlayerRegistration'
import GamePlay from './components/GamePlay'
import GameResult from './components/GameResult'
import LeaderBoard from './components/Leaderboard'
import TrafficNetwork from './components/TrafficNetwork'
import TrafficDisplay from './components/trafficdisplay'
import EightQueensGame from './components/EightQueensGame';
import SnakeLadder from './SnakeLadder';
import TowerOfHanoiBase from "./pages/TowerOfHanoiBase";

function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
            <Route path="/gameDisplay" element={<GameDisplay />}/>
            <Route path="/tspGame" element={<Tsp />}/>
            <Route path="/trc" element={<Traffic />}/>
            <Route path="/gamePlay" element={<GamePlay />}/>
            <Route path="/gameResult" element={<GameResult />}/>
            <Route path="/leaderBoard" element={<LeaderBoard />}/>
            <Route path="/trafficNetwork" element={<TrafficNetwork />}/>
            <Route path="/trafficDisplay" element={<TrafficDisplay />}/>
            <Route path="/eight-queens" element={<EightQueensGame />}/>
            <Route path="/snake-ladder" element={<SnakeLadder />}/>
            <Route path="/towerOfHanoi" element={<TowerOfHanoiBase />}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App;
