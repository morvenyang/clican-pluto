//
//  MapLayer.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Character.h"

@interface MapLayer : CCLayer {
    Character* _char1;
}

@property (nonatomic,retain) Character* char1;

+(CCScene *) scene;

@end
