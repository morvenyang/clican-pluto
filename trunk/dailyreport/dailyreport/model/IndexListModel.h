//
//  IndexListModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "BaseModel.h"
@interface IndexListModel : BaseModel{
    NSMutableArray* _brandList;
}
@property (nonatomic, retain) NSMutableArray* brandList;
@end
