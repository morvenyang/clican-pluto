//
//  Character.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//  这个类是用来描述游戏人物的模型
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Mobility.h"
#import "PositionUtil.h"

#import "GlobalEventHandler.h"



@interface Character : NSObject {
    Mobility* _landMobility;
    Mobility* _airMobility;
    CCSprite* _characterSprite;
    UIImage* _sourceCharacterImage;
    UIImage* _grayCharacterImage;
    Position* _sourcePosition;
    CCArray* _weapens;
    NSMutableSet* _attackRange;
    BOOL _canMountHorse;
    BOOL _mountHorse;
    BOOL _selected;
    BOOL _finished;
}

@property (nonatomic,retain) Mobility* landMobility;
@property (nonatomic,retain) Mobility* airMobility;
@property (nonatomic,retain) CCSprite* characterSprite;
@property (nonatomic,retain) UIImage* sourceCharacterImage;
@property (nonatomic,retain) UIImage* grayCharacterImage;
@property (nonatomic,retain) Position* sourcePosition;
@property (nonatomic,retain) CCArray* weapens;
@property (nonatomic,retain) NSMutableSet* attackRange;
@property (nonatomic,assign)  BOOL canMountHorse;
@property (nonatomic,assign)  BOOL mountHorse;
@property (nonatomic,assign)  BOOL selected;
@property (nonatomic,assign)  BOOL finished;

+(id)characterWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile position:(Position*) position;

@end


