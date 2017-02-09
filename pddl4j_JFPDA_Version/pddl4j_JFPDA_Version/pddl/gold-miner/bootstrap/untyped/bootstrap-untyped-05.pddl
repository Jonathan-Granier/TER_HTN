(define (problem gold-miner-bootstrap-3x3-05-untyped)
(:domain gold-miner-typed)
(:objects 
        f0-0f f0-1f f0-2f 
        f1-0f f1-1f f1-2f 
        f2-0f f2-1f f2-2f 
)
(:init
(arm-empty)
(connected f0-0f f0-1f)
(connected f0-1f f0-2f)
(connected f1-0f f1-1f)
(connected f1-1f f1-2f)
(connected f2-0f f2-1f)
(connected f2-1f f2-2f)
(connected f0-0f f1-0f)
(connected f0-1f f1-1f)
(connected f0-2f f1-2f)
(connected f1-0f f2-0f)
(connected f1-1f f2-1f)
(connected f1-2f f2-2f)
(connected f0-1f f0-0f)
(connected f0-2f f0-1f)
(connected f1-1f f1-0f)
(connected f1-2f f1-1f)
(connected f2-1f f2-0f)
(connected f2-2f f2-1f)
(connected f1-0f f0-0f)
(connected f1-1f f0-1f)
(connected f1-2f f0-2f)
(connected f2-0f f1-0f)
(connected f2-1f f1-1f)
(connected f2-2f f1-2f)
(robot-at f0-0f)
(clear f0-0f)
(soft-rock-at f0-1f)
(soft-rock-at f0-2f)
(bomb-at f1-0f)
(laser-at f1-0f)
(clear f1-0f)
(soft-rock-at f1-1f)
(gold-at f1-2f)
(soft-rock-at f1-2f)
(clear f2-0f)
(soft-rock-at f2-1f)
(soft-rock-at f2-2f)
)
(:goal
(holds-gold)
)
)