import React, { useMemo } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Text } from '@react-three/drei';
import * as THREE from 'three';

const CELL_SIZE = 2;

const getPosition = (cell, size) => {
    let r = Math.floor((cell - 1) / size);
    let c = (cell - 1) % size;
    if (r % 2 === 1) c = size - 1 - c;

    const x = (c - size / 2 + 0.5) * CELL_SIZE;
    const z = ((size / 2) - 0.5 - r) * CELL_SIZE;
    return new THREE.Vector3(x, 0.05, z);
};

const Tile = ({ x, z, color, number }) => (
    <group position={[x, 0, z]}>
        <mesh>
            <boxGeometry args={[CELL_SIZE * 0.95, 0.1, CELL_SIZE * 0.95]} />
            <meshStandardMaterial color={color} roughness={0.8} />
        </mesh>
        <Text
            position={[0, 0.08, 0]}
            rotation={[-Math.PI / 2, 0, 0]}
            fontSize={0.5}
            color="#1e293b"
            anchorX="center"
            anchorY="middle"
            fontWeight="bold"
        >
            {number}
        </Text>
    </group>
);

// 3D-looking Snake with rings viewed from top
const Snake3D = ({ start, end, size, index }) => {
    const startPos = getPosition(start, size);
    const endPos = getPosition(end, size);

    const curve = useMemo(() => {
        const mid = new THREE.Vector3().addVectors(startPos, endPos).multiplyScalar(0.5);
        const sideShift = (index % 2 === 0 ? 0.4 : -0.4);

        const p1 = new THREE.Vector3().lerpVectors(startPos, mid, 0.4);
        p1.x += sideShift;

        const p2 = new THREE.Vector3().lerpVectors(mid, endPos, 0.4);
        p2.x -= sideShift;

        return new THREE.CatmullRomCurve3([startPos, p1, mid, p2, endPos]);
    }, [startPos, endPos, index]);

    const snakeColor = index % 2 === 0 ? '#dc2626' : '#15803d';
    const ringColor = index % 2 === 0 ? '#7f1d1d' : '#14532d';

    return (
        <group>
            {/* Main body tube */}
            <mesh>
                <tubeGeometry args={[curve, 50, 0.18, 12, false]} />
                <meshStandardMaterial color={snakeColor} roughness={0.4} metalness={0.1} />
            </mesh>

            {/* Rings around the body */}
            {Array.from({ length: 8 }).map((_, i) => {
                const t = (i + 1) / 9;
                const point = curve.getPoint(t);
                const tangent = curve.getTangent(t);
                const axis = new THREE.Vector3(0, 1, 0);
                const quaternion = new THREE.Quaternion().setFromUnitVectors(axis, tangent);

                return (
                    <mesh key={i} position={point} quaternion={quaternion}>
                        <cylinderGeometry args={[0.2, 0.2, 0.1, 12]} />
                        <meshStandardMaterial color={ringColor} roughness={0.3} />
                    </mesh>
                );
            })}

            {/* Head */}
            <mesh position={startPos}>
                <sphereGeometry args={[0.25, 16, 16]} />
                <meshStandardMaterial color={snakeColor} roughness={0.3} />
            </mesh>

            {/* Eyes */}
            <mesh position={startPos.clone().add(new THREE.Vector3(0.1, 0.15, 0.12))}>
                <sphereGeometry args={[0.05]} />
                <meshStandardMaterial color="white" />
            </mesh>
            <mesh position={startPos.clone().add(new THREE.Vector3(0.1, 0.15, 0.12)).add(new THREE.Vector3(0, 0, 0.03))}>
                <sphereGeometry args={[0.02]} />
                <meshStandardMaterial color="black" />
            </mesh>
            <mesh position={startPos.clone().add(new THREE.Vector3(-0.1, 0.15, 0.12))}>
                <sphereGeometry args={[0.05]} />
                <meshStandardMaterial color="white" />
            </mesh>
            <mesh position={startPos.clone().add(new THREE.Vector3(-0.1, 0.15, 0.12)).add(new THREE.Vector3(0, 0, 0.03))}>
                <sphereGeometry args={[0.02]} />
                <meshStandardMaterial color="black" />
            </mesh>

            {/* Tongue */}
            <group position={startPos.clone().add(new THREE.Vector3(0, 0, 0.22))} rotation={[0.3, 0, 0]}>
                <mesh position={[0.02, -0.03, 0]} rotation={[0, 0, -0.1]}>
                    <cylinderGeometry args={[0.008, 0.004, 0.15]} />
                    <meshStandardMaterial color="#ef4444" />
                </mesh>
                <mesh position={[-0.02, -0.03, 0]} rotation={[0, 0, 0.1]}>
                    <cylinderGeometry args={[0.008, 0.004, 0.15]} />
                    <meshStandardMaterial color="#ef4444" />
                </mesh>
            </group>
        </group>
    );
};

// 3D Ladder
const Ladder3D = ({ start, end }) => {
    const vec = new THREE.Vector3().subVectors(end, start);
    const len = vec.length();
    const mid = new THREE.Vector3().addVectors(start, end).multiplyScalar(0.5);
    mid.y += 0.2;

    const axis = new THREE.Vector3(0, 1, 0);
    const quaternion = new THREE.Quaternion().setFromUnitVectors(axis, vec.clone().normalize());

    return (
        <group position={mid} quaternion={quaternion}>
            {/* Rails - wooden cylinders */}
            <mesh position={[0.25, 0, 0]}>
                <cylinderGeometry args={[0.06, 0.06, len, 8]} />
                <meshStandardMaterial color="#92400e" roughness={0.8} />
            </mesh>
            <mesh position={[-0.25, 0, 0]}>
                <cylinderGeometry args={[0.06, 0.06, len, 8]} />
                <meshStandardMaterial color="#92400e" roughness={0.8} />
            </mesh>

            {/* Rungs */}
            {Array.from({ length: Math.floor(len / 0.6) }).map((_, i) => (
                <mesh key={i} position={[0, -len / 2 + (i + 1) * 0.6, 0]} rotation={[0, 0, Math.PI / 2]}>
                    <cylinderGeometry args={[0.04, 0.04, 0.5, 8]} />
                    <meshStandardMaterial color="#b45309" roughness={0.7} />
                </mesh>
            ))}
        </group>
    );
};

const PlayerToken = ({ pos, size, color, label }) => {
    const targetPos = getPosition(pos, size);

    return (
        <group position={[targetPos.x, 0.4, targetPos.z]}>
            {/* 3D disc with slight height */}
            <mesh>
                <cylinderGeometry args={[0.35, 0.35, 0.15, 32]} />
                <meshStandardMaterial color={color} roughness={0.3} metalness={0.6} />
            </mesh>
            {/* Label */}
            <Text
                position={[0, 0.12, 0]}
                rotation={[-Math.PI / 2, 0, 0]}
                fontSize={0.35}
                color="white"
                anchorX="center"
                anchorY="middle"
                fontWeight="bold"
            >
                {label}
            </Text>
        </group>
    );
};

const Board3D = ({ size, snakes, ladders, playerPos, computerPos }) => {

    const tiles = useMemo(() => {
        const arr = [];
        for (let i = 1; i <= size * size; i++) {
            const pos = getPosition(i, size);
            let r = Math.floor((i - 1) / size);
            let c = (i - 1) % size;
            const isDark = (r + c) % 2 === 1;
            const color = isDark ? '#a8a29e' : '#f5f5f4';
            arr.push({ id: i, pos, color, number: i });
        }
        return arr;
    }, [size]);

    return (
        <div style={{
            width: '100%',
            height: '600px',
            background: 'linear-gradient(135deg, #1e293b 0%, #0f172a 100%)',
            borderRadius: '20px',
            overflow: 'hidden',
            boxShadow: '0 10px 40px rgba(0,0,0,0.3)'
        }}>
            <Canvas
                orthographic
                camera={{
                    position: [0, 50, 0],
                    zoom: 28,
                    near: 0.1,
                    far: 1000,
                    up: [0, 0, -1]
                }}
            >
                {/* Soft lighting for 3D effect */}
                <ambientLight intensity={0.6} />
                <directionalLight position={[10, 20, 10]} intensity={0.8} />
                <directionalLight position={[-10, 20, -10]} intensity={0.4} />

                {/* Locked Controls */}
                <OrbitControls
                    enableRotate={false}
                    enableZoom={false}
                    enablePan={false}
                />

                <group position={[0, -5, 0]}>
                    {/* Board */}
                    {tiles.map((t) => (
                        <Tile key={t.id} x={t.pos.x} z={t.pos.z} color={t.color} number={t.number} />
                    ))}

                    {/* Board Base */}
                    <mesh position={[0, -0.1, 0]}>
                        <boxGeometry args={[size * CELL_SIZE + 0.8, 0.1, size * CELL_SIZE + 0.8]} />
                        <meshStandardMaterial color="#57534e" roughness={0.9} />
                    </mesh>

                    {/* Game Elements - 3D */}
                    {snakes.map((s, i) => (
                        <Snake3D key={`s-${i}`} start={s.start} end={s.end} size={size} index={i} />
                    ))}
                    {ladders.map((l, i) => (
                        <Ladder3D key={`l-${i}`} start={getPosition(l.start, size)} end={getPosition(l.end, size)} />
                    ))}

                    {/* Tokens */}
                    <PlayerToken pos={playerPos} size={size} color="#3b82f6" label="YOU" />
                    {computerPos && <PlayerToken pos={computerPos} size={size} color="#ef4444" label="AI" />}
                </group>
            </Canvas>
        </div>
    );
};

export default Board3D;
