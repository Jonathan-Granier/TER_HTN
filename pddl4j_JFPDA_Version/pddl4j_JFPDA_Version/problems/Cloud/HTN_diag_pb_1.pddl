
(define (problem HTN_diag_1)
(:domain DIAGNOSIS)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects 	vm1 vm2 - vMachine
            v-center1 v-center2 - vCenter
            v-sphere - vSphere
            debian-5 debian-6 winxppro - template)
(:init
		(disconnected-from v-center1 v-sphere)
	    (disconnected-from v-center2 v-sphere)
	    (used v-sphere)		
	    (stopped v-sphere)
	    (out-of-date v-sphere)
	    ;;(up-to-date v-sphere)
	    (empty-logins v-sphere)    
	   	(empty-ip-address v-sphere)  
	    (available debian-5 v-sphere)   
	    (available debian-6 v-sphere) 	
	    (available winxppro v-sphere)   
)

(:goal
       :tasks(
       			(tag t1 (vm-creation vm1 v-center2 debian-5))
       	     )
       :constraints (and 
       					(after (and (deployed debian-5 vm1 v-center2 v-sphere)) t1)
					)
)

