//
//  FightMenuLayer.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "FightMenuLayer.h"
#import "EventDispatcher.h"
#import "MapLayer.h"
#import "WorkflowConstants.h"
#import "PositionUtil.h"

@implementation FightMenuLayer

@synthesize fightMenu = _fightMenu;
@synthesize attack = _attack;
@synthesize cancel = _cancel;
@synthesize standby = _standby;
@synthesize mountHorse = _mountHorse;
@synthesize dismountHorse = _dismountHorse;
@synthesize item = _item;

static FightMenuLayer* sharedFightMenuLayer = nil;





+(FightMenuLayer*) sharedFightMenuLayer
{
	@synchronized(self) {
		if (sharedFightMenuLayer == nil){
            sharedFightMenuLayer = [FightMenuLayer node]; // assignment not done here
            
            [CCMenuItemFont setFontSize:20];
            sharedFightMenuLayer.attack = [CCMenuItemFont 
                                           itemFromString:@"攻击" target:sharedFightMenuLayer selector:@selector(attack:)];
            sharedFightMenuLayer.item = [CCMenuItemFont 
                                           itemFromString:@"道具" target:sharedFightMenuLayer selector:@selector(item:)];
            sharedFightMenuLayer.mountHorse = [CCMenuItemFont 
                                         itemFromString:@"上马" target:sharedFightMenuLayer selector:@selector(mountHorse:)];
            sharedFightMenuLayer.dismountHorse = [CCMenuItemFont 
                                         itemFromString:@"下马" target:sharedFightMenuLayer selector:@selector(dismountHorse:)];
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

-(void) showAtPosition:(Position*) position character:(Character*) character{
    CCLOG(@"position%@",position);
    self.fightMenu.position = [position toCenterCGPoint];
    if(character.canMountHorse){
        if(character.mountHorse){
            self.mountHorse.visible = NO;
            self.dismountHorse.visible = YES;
        } else {
            self.mountHorse.visible = YES;
            self.dismountHorse.visible = NO;
        }
    }else{
        self.mountHorse.visible = NO;
        self.dismountHorse.visible = NO;
    }
    if([PositionUtil canAttack:position targetCharacterArray:[MapLayer sharedMapLayer].enemyCharacterArray rangeSet:character.attackRange]){
        self.attack.visible= YES;
    }else {
        self.attack.visible= NO;
    }
    self.visible = YES;
    [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
}

-(void) hide{
    self.visible = NO;
     [[GlobalEventHandler sharedHandler] removePositionTouchDelegate:self];
}

-(void) attack:(id) sender {
    CCLOG(@"attack");

    [self hide];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_FM_ATTACK_ONCLICK forParameters:nil];
}

-(void) item:(id) sender {
    CCLOG(@"item");

    [self hide];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_FM_ITEM_ONCLICK forParameters:nil];
}

-(void) mountHorse:(id) sender {
    CCLOG(@"mountHorse");

    [self hide];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_FM_MOUNTHOUSE_ONCLICK forParameters:nil];
}

-(void) dismountHorse:(id) sender {
    CCLOG(@"dismountHorse");

    [self hide];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_FM_DISMOUNTHOUSE_ONCLICK forParameters:nil];
}

-(void) standby:(id) sender {
    CCLOG(@"standby");
    
    [self hide];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_FM_STANDBY_ONCLICK forParameters:nil];
}

-(void) cancel:(id) sender {
    CCLOG(@"cancel");

    [self hide];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_FM_CANCEL_ONCLICK forParameters:nil];
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
