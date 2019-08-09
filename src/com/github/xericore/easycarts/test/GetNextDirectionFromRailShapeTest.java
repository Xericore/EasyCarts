package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.data.RailTrace;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class GetNextDirectionFromRailShapeTest
{
    @Test
    public void getNextFacingFromRailShape_given_railNS_facingNorth_returnsNorth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_SOUTH, BlockFace.NORTH);
        Assert.assertEquals(BlockFace.NORTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNS_facingSouth_returnsSouth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_SOUTH, BlockFace.SOUTH);
        Assert.assertEquals(BlockFace.SOUTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNS_facingWest_returnsWest()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_SOUTH, BlockFace.WEST);
        Assert.assertEquals(BlockFace.WEST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNS_facingEast_returnsEast()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_SOUTH, BlockFace.EAST);
        Assert.assertEquals(BlockFace.EAST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railEW_facingEast_returnsEast()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.EAST_WEST, BlockFace.EAST);
        Assert.assertEquals(BlockFace.EAST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railEW_facingWest_returnsWest()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.EAST_WEST, BlockFace.WEST);
        Assert.assertEquals(BlockFace.WEST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railEW_facingNorth_returnsNorth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.EAST_WEST, BlockFace.NORTH);
        Assert.assertEquals(BlockFace.NORTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railEW_facingSouth_returnsSouth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.EAST_WEST, BlockFace.SOUTH);
        Assert.assertEquals(BlockFace.SOUTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNE_facingSouth_returnsEast()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_EAST, BlockFace.SOUTH);
        Assert.assertEquals(BlockFace.EAST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNE_facingWest_returnsNorth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_EAST, BlockFace.WEST);
        Assert.assertEquals(BlockFace.NORTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNW_facingSouth_returnsWest()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_WEST, BlockFace.SOUTH);
        Assert.assertEquals(BlockFace.WEST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railNW_facingEast_returnsNorth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.NORTH_WEST, BlockFace.EAST);
        Assert.assertEquals(BlockFace.NORTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railSE_facingNorth_returnsEast()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.SOUTH_EAST, BlockFace.NORTH);
        Assert.assertEquals(BlockFace.EAST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railSE_facingWest_returnsSouth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.SOUTH_EAST, BlockFace.WEST);
        Assert.assertEquals(BlockFace.SOUTH, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railSW_facingNorth_returnsWest()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.SOUTH_WEST, BlockFace.NORTH);
        Assert.assertEquals(BlockFace.WEST, nextFacing);
    }

    @Test
    public void getNextFacingFromRailShape_given_railSW_facingEast_returnsSouth()
    {
        BlockFace nextFacing = RailTrace.getNextFacingFromRailShape(Rail.Shape.SOUTH_WEST, BlockFace.EAST);
        Assert.assertEquals(BlockFace.SOUTH, nextFacing);
    }

}
