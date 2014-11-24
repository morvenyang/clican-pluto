//
//  StoreSumModel.h
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "BaseModel.h"
#import "StoreSum.h"
@protocol StoreSumDelegate;
@interface StoreSumModel : BaseModel{
    NSString* _brand;
    id<StoreSumDelegate> _delegate;
}
@property(nonatomic,assign) id<StoreSumDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;
@end

@protocol StoreSumDelegate <NSObject>

- (void) brandDidFinishLoad:(NSArray*) monthlySums yearlySums:(NSArray*)yearlySums date:(NSDate*) date;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
