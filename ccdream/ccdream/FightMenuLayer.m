//
//  FightMenuLayer.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "FightMenuLayer.h"


@implementation FightMenuLayer

@synthesize fightMenu = _fightMenu;
@synthesize attack = _attack;
@synthesize cancel = _cancel;
@synthesize standby = _standby;
@synthesize selectAttack = _selectAttack;

static FightMenuLayer* sharedFightMenuLayer = nil;





+(FightMenuLayer*) sharedFightMenuLayer
{
	@synchronized(self) {
		if (sharedFightMenuLayer == nil){
            sharedFightMenuLayer = [FightMenuLayer node]; // assignment not done here
            
            
            sharedFightMenuLayer.attack = [CCMenuItemFont 
                                           itemFromString:@"攻击" target:sharedFightMenuLayer selector:@selector(attack:)];
            sharedFightMenuLayer.standby = [CCMenuItemFont 
                                           itemFromString:@"待机" target:sharedFightMenuLayer selector:@selector(standby:)];
            sharedFightMenuLayer.cancel = [CCMenuItemFont 
                                           itemFromString:@"取消" target:sharedFightMenuLayer selector:@selector(cancel:)];
            sharedFightMenuLayer.fightMenu = [CCMenu menuWithItems:sharedFightMenuLayer.attack,sharedFightMenuLayer.standby,sharedFightMenuLayer.cancel,nil];
            [sharedFightMenuLayer.fightMenu alignItemsVertically];
            sharedFightMenuLayer.fightMenu.position = ccp(100, 100);
            [sharedFightMenuLayer addChild:sharedFightMenuLayer.fightMenu];
        }
	}
	return sharedFightMenuLayer;
}

-(void) showAtPosition:(Position*) position{
    CCLOG(@"position%@",position);
    self.fightMenu.position = [position toCenterCGPoint];
    self.visible = YES;
     [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
}

-(void) hide{
    self.visible = NO;
     [[GlobalEventHandler sharedHandler] removePositionTouchDelegate:self];
}

-(void) attack:(id) sender {
    CCLOG(@"attack");
    self.selectAttack = YES;
    [self hide];
}

-(void) standby:(id) sender {
    CCLOG(@"standby");
    self.selectAttack = NO;
    [self hide];
    
}

-(void) cancel:(id) sender {
    CCLOG(@"cancel");
    self.selectAttack = NO;
    [self hide];
}

- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    return YES;
}

- (void)dealloc {
    [_fightMenu release];
    _fightMenu = nil;
    [_attack release];
    _attack = nil;
    [_standby release];
    _standby = nil;
    [_cancel release];
    _cancel = nil;
    [super dealloc];
}


@end
