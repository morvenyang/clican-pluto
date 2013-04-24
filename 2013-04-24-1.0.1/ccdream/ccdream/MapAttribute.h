//
//  MapAttribute.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Position.h"
@interface MapAttribute : NSObject {
    CCArray* _playerBeginPosiArray;
    CCArray* _enemyBeginPosiArray;
}

@property (nonatomic,retain) CCArray* playerBeginPosiArray;
@property (nonatomic,retain) CCArray* enemyBeginPosiArray;

+(id) mapAttributeWith:(CCTMXTiledMap*) tiledMap;
@end
